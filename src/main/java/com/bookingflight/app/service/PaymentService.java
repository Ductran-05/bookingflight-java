package com.bookingflight.app.service;

import com.bookingflight.app.config.VNPayConfig;
import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Payment;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.PaymentRequest;
import com.bookingflight.app.dto.response.PaymentResponse;
import com.bookingflight.app.dto.response.PaymentUrlResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.PaymentMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.PaymentRepository;
import com.bookingflight.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final AccountRepository accountRepository;
    private final VNPayConfig vnPayConfig;
    private final ResultPanigationMapper resultPanigationMapper;

    @Transactional
    public PaymentUrlResponse createPaymentUrl(PaymentRequest request, HttpServletRequest servletRequest) {
        try {
            // Get current user email from SecurityContext
            String currentUserEmail = SecurityUtil.getCurrentUserLogin().isPresent()
                    ? SecurityUtil.getCurrentUserLogin().get()
                    : "";
            
            Account account = accountRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            // Generate unique transaction reference
            String txnRef;
            int attempts = 0;
            do {
                txnRef = generateUniqueTxnRef();
                attempts++;
                if (attempts > 10) {
                    throw new AppException(ErrorCode.UNIDENTIFIED_EXCEPTION);
                }
            } while (paymentRepository.findByTxnRef(txnRef).isPresent());

            // Create payment record
            Payment payment = Payment.builder()
                    .amount(request.getAmount())
                    .orderInfo(request.getOrderInfo())
                    .txnRef(txnRef)
                    .status(Payment.PaymentStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .account(account)
                    .build();

            paymentRepository.save(payment);

            // VNPay parameters
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TmnCode = vnPayConfig.vnp_TmnCode;
            String vnp_IpAddr = vnPayConfig.getIpAddress(servletRequest);
            
            int amount = request.getAmount() * 100;
            
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            

            vnp_Params.put("vnp_TxnRef", txnRef);
            vnp_Params.put("vnp_OrderInfo", request.getOrderInfo());
            vnp_Params.put("vnp_OrderType", "other");
            
            if (request.getLanguage() != null && !request.getLanguage().isEmpty()) {
                vnp_Params.put("vnp_Locale", request.getLanguage());
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }
            
            vnp_Params.put("vnp_ReturnUrl", vnPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            
            String vnp_CreateDate = vnPayConfig.getCurrentDateTime();
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            
            String vnp_ExpireDate = vnPayConfig.getExpireDateTime();
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            
            // Build data to hash and query string
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            
            String queryUrl = query.toString();
            String vnp_SecureHash = vnPayConfig.hmacSHA512(vnPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = vnPayConfig.vnp_PayUrl + "?" + queryUrl;
            
            return new PaymentUrlResponse("00", "success", paymentUrl, txnRef);
            
        } catch (UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.UNIDENTIFIED_EXCEPTION);
        }
    }

    private String generateUniqueTxnRef() {
        // Combine timestamp and random number for better uniqueness
        long timestamp = System.currentTimeMillis();
        String timestampStr = String.valueOf(timestamp);
        String randomPart = vnPayConfig.getRandomNumber(4);
        // Take last 4 digits of timestamp + 4 random digits = 8 digits total
        return timestampStr.substring(timestampStr.length() - 4) + randomPart;
    }

    @Transactional
    public PaymentResponse handlePaymentReturn(Map<String, String> params) {
        String vnp_TxnRef = params.get("vnp_TxnRef");
        String vnp_Amount = params.get("vnp_Amount");
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TransactionNo = params.get("vnp_TransactionNo");
        String vnp_BankCode = params.get("vnp_BankCode");
        String vnp_CardType = params.get("vnp_CardType");
        String vnp_SecureHash = params.get("vnp_SecureHash");

        // Verify signature
        Map<String, String> fields = new HashMap<>(params);
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    throw new AppException(ErrorCode.UNIDENTIFIED_EXCEPTION);
                }
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }
        
        String secureHash = vnPayConfig.hmacSHA512(vnPayConfig.vnp_HashSecret, hashData.toString());
        
        if (!secureHash.equals(vnp_SecureHash)) {
            throw new AppException(ErrorCode.UNIDENTIFIED_EXCEPTION);
        }

        // Update payment status
        Payment payment = paymentRepository.findByTxnRef(vnp_TxnRef)
                .orElseThrow(() -> new AppException(ErrorCode.UNIDENTIFIED_EXCEPTION));

        if ("00".equals(vnp_ResponseCode)) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }

        payment.setVnpTransactionNo(vnp_TransactionNo);
        payment.setBankCode(vnp_BankCode);
        payment.setCardType(vnp_CardType);

        paymentRepository.save(payment);

        return paymentMapper.toPaymentResponse(payment);
    }

    public PaymentResponse getPaymentByTxnRef(String txnRef) {
        Payment payment = paymentRepository.findByTxnRef(txnRef)
                .orElseThrow(() -> new AppException(ErrorCode.UNIDENTIFIED_EXCEPTION));
        return paymentMapper.toPaymentResponse(payment);
    }

    public ResultPaginationDTO getAllPayments(Specification<Payment> spec, Pageable pageable) {
        Page<PaymentResponse> page = paymentRepository.findAll(spec, pageable)
                .map(paymentMapper::toPaymentResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }
} 
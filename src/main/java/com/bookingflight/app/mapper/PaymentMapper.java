package com.bookingflight.app.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Payment;
import com.bookingflight.app.dto.response.PaymentResponse;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMapper {

    public PaymentResponse toPaymentResponse(Payment payment) {
        // Convert String orderInfo to List<String> by splitting on spaces
        List<String> orderInfoList = payment.getOrderInfo() != null && !payment.getOrderInfo().trim().isEmpty()
                ? Arrays.asList(payment.getOrderInfo().split(" "))
                : List.of();
        
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                orderInfoList,
                payment.getTxnRef(),
                payment.getStatus(),
                payment.getVnpTransactionNo(),
                payment.getBankCode(),
                payment.getCardType(),
                payment.getCreatedAt(),
                payment.getPaidAt(),
                payment.getAccount() != null ? payment.getAccount().getId() : null);
    }
    
    // Helper method to convert List<String> to String (joining with spaces)
    public String joinOrderInfo(List<String> orderInfoList) {
        return orderInfoList != null && !orderInfoList.isEmpty()
                ? String.join(" ", orderInfoList)
                : "";
    }
}
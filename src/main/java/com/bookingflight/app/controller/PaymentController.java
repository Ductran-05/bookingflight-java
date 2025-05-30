package com.bookingflight.app.controller;

import com.bookingflight.app.dto.request.PaymentRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.PaymentResponse;
import com.bookingflight.app.dto.response.PaymentUrlResponse;
import com.bookingflight.app.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<APIResponse<PaymentUrlResponse>> createPayment(
            @Valid @RequestBody PaymentRequest request,
            HttpServletRequest httpRequest) {
        PaymentUrlResponse response = paymentService.createPaymentUrl(request, httpRequest);
        APIResponse<PaymentUrlResponse> apiResponse = APIResponse.<PaymentUrlResponse>builder()
                .Code(200)
                .Message("Payment URL created successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<APIResponse<PaymentResponse>> vnpayReturn(@RequestParam Map<String, String> params) {
        PaymentResponse response = paymentService.handlePaymentReturn(params);
        APIResponse<PaymentResponse> apiResponse = APIResponse.<PaymentResponse>builder()
                .Code(200)
                .Message("Payment processed successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/status/{txnRef}")
    public ResponseEntity<APIResponse<PaymentResponse>> getPaymentStatus(@PathVariable String txnRef) {
        PaymentResponse response = paymentService.getPaymentByTxnRef(txnRef);
        APIResponse<PaymentResponse> apiResponse = APIResponse.<PaymentResponse>builder()
                .Code(200)
                .Message("Payment status retrieved successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
} 
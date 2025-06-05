package com.bookingflight.app.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Payment;
import com.bookingflight.app.dto.response.PaymentResponse;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMapper {

    public PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getOrderInfo(),
                payment.getTxnRef(),
                payment.getStatus(),
                payment.getVnpTransactionNo(),
                payment.getBankCode(),
                payment.getCardType(),
                payment.getCreatedAt(),
                payment.getPaidAt(),
                payment.getAccount() != null ? payment.getAccount().getId() : null);
    }
}
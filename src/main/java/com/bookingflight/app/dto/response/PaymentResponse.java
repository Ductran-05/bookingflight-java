package com.bookingflight.app.dto.response;

import com.bookingflight.app.domain.Payment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String id;
    int amount;
    String orderInfo;
    String txnRef;
    Payment.PaymentStatus status;
    String vnpTransactionNo;
    String bankCode;
    String cardType;
    LocalDateTime createdAt;
    LocalDateTime paidAt;
    String accountId;
} 
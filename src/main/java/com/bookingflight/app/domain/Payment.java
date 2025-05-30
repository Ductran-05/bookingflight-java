package com.bookingflight.app.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    int amount;

    @Column(nullable = false)
    String orderInfo;

    @Column(unique = true, nullable = false)
    String txnRef;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    String vnpTransactionNo;

    String bankCode;

    String cardType;

    @Column(nullable = false)
    LocalDateTime createdAt;

    LocalDateTime paidAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED,
        CANCELLED
    }
}

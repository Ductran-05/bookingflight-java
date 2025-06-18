package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByTxnRef(String txnRef);

    void deleteByAccount(Account account);
}
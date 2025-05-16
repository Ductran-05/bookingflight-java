package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String>, JpaSpecificationExecutor<Ticket> {
}

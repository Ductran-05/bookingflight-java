package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.domain.TicketStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String>, JpaSpecificationExecutor<Ticket> {

    Page<Ticket> findAllByAccount(Account currAccount, Specification<Flight> spec, Pageable pageable);

    int countByFlightIdAndSeatIdAndIsBookedFalse(String flightId, String seatId);

    Ticket findFirstByFlightIdAndSeatIdAndIsBookedFalse(String flightId, String seatId);

    List<Ticket> findAllByFlightId(String id);

    Integer countByFlightIdAndSeatIdAndIsBookedTrue(String id, String id2);

    void deleteAllByFlightId(String id);

    boolean existsByFlightId(String id);

    int countByFlightIdAndTicketStatus(String id, TicketStatus available);
}

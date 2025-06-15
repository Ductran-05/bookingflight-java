package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Airline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, String>, JpaSpecificationExecutor<Airline> {
    boolean existsByAirlineCode(String code);

    @Query("SELECT a.id, a.airlineName, COUNT(t.id) " +
            "FROM Airline a " +
            "LEFT JOIN Plane p ON p.airline.id = a.id " +
            "LEFT JOIN Flight f ON f.plane.id = p.id " +
            "LEFT JOIN Ticket t ON t.flight.id = f.id " +
            "GROUP BY a.id, a.airlineName")
    List<Object[]> countTicketsByAirline();
}

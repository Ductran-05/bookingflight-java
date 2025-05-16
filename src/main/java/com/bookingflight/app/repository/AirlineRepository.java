package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, String>, JpaSpecificationExecutor<Airline> {
    boolean existsByAirlineCode(String code);
}

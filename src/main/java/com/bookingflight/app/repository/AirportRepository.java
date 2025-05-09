package com.bookingflight.app.repository;

import com.bookingflight.app.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
    boolean existsByAirportCode(String airportCode);
}

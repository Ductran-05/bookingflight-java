package com.bookingflight.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookingflight.app.domain.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {

    Optional<Flight> findByFlightCode(String flightCode);

}

package com.bookingflight.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.domain.Plane;

@Repository
public interface Flight_AirportRepository extends JpaRepository<Flight_Airport, String> {
    List<Flight_Airport> findAllByFlightId(String id);

    Optional<Plane> findByFlightId(String id);

    void deleteAllByFlightId(String id);

    Boolean existsByAirport(Airport airport);
}

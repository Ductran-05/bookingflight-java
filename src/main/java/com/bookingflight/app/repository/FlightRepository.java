package com.bookingflight.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Plane;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String>, JpaSpecificationExecutor<Flight> {

    Optional<Flight> findByFlightCode(String flightCode);

    Boolean existsByPlane(Plane plane);

    boolean existsByDepartureAirport(Airport airport);

    boolean existsByArrivalAirport(Airport airport);

}

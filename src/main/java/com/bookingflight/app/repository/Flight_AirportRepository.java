package com.bookingflight.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookingflight.app.domain.Flight_Airport;

@Repository
public interface Flight_AirportRepository extends JpaRepository<Flight_Airport, String> {
    List<Flight_Airport> findAllByFlightId(String id);
}

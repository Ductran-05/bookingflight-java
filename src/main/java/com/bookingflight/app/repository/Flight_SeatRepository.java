package com.bookingflight.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookingflight.app.domain.Flight_Seat;

@Repository
public interface Flight_SeatRepository extends JpaRepository<Flight_Seat, String> {

    List<Flight_Seat> getAllFlight_SeatByFlightId(String id);

    List<Flight_Seat> findAllByFlightId(String flightId);

}

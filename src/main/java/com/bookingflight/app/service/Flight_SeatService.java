package com.bookingflight.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Flight_Seat;
import com.bookingflight.app.dto.request.Flight_SeatRequest;
import com.bookingflight.app.dto.response.Flight_SeatResponse;
import com.bookingflight.app.mapper.Flight_SeatMapper;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.Flight_SeatRepository;
import com.bookingflight.app.repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Flight_SeatService {
    private final Flight_SeatRepository flight_SeatRepository;
    private final Flight_SeatMapper flight_SeatMapper;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    public Flight_SeatResponse createFlight_Seat(Flight_SeatRequest request) {
        Flight_Seat flight_Seat = flight_SeatMapper.toFlight_Seat(request, flightRepository, seatRepository);
        flight_SeatRepository.save(flight_Seat);
        return flight_SeatMapper.toFlight_SeatResponse(flight_Seat, flightRepository, seatRepository);
    }
}

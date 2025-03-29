package com.bookingflight.app.service;

import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.dto.request.Flight_AirportRequest;
import com.bookingflight.app.dto.response.Flight_AirportResponse;
import com.bookingflight.app.mapper.Flight_AirportMapper;
import com.bookingflight.app.repository.AirportRepository;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.Flight_AirportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Flight_AirportService {
    private final Flight_AirportMapper flight_AirportMapper;
    private final Flight_AirportRepository flight_AirportRepository;
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;

    public Flight_AirportResponse createFlight_Airport(Flight_AirportRequest request) {
        Flight_Airport flight_Airport = flight_AirportMapper.toFlight_Airport(request, flightRepository,
                airportRepository);

        flight_AirportRepository.save(flight_Airport);
        return flight_AirportMapper.toFlight_AirportResponse(flight_Airport, flightRepository,
                airportRepository);
    }
}

package com.bookingflight.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.dto.request.Flight_AirportRequest;
import com.bookingflight.app.dto.response.Flight_AirportResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
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

    public Flight_AirportResponse getFlight_AirportById(String id) {
        Flight_Airport flight_Airport = flight_AirportRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.FLIGHT_AIRPORT_NOT_EXISTED));
        return flight_AirportMapper.toFlight_AirportResponse(flight_Airport, flightRepository,
                airportRepository);
    }

    public Flight_AirportResponse updateFlight_Airport(String id, Flight_AirportRequest request) {
        Flight_Airport flight_Airport = flight_AirportRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.FLIGHT_AIRPORT_NOT_EXISTED));
        flight_AirportMapper.toFlight_Airport(request, flightRepository, airportRepository);
        flight_Airport.setId(id);
        flight_AirportRepository.save(flight_Airport);
        return flight_AirportMapper.toFlight_AirportResponse(flight_Airport, flightRepository,
                airportRepository);
    }

    public void deleteFlight_Airport(String id) {
        Flight_Airport flight_Airport = flight_AirportRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.FLIGHT_AIRPORT_NOT_EXISTED));
        flight_AirportRepository.delete(flight_Airport);
    }

    public List<Flight_AirportResponse> getAllFlight_Airports() {
        List<Flight_AirportResponse> flight_Airports = flight_AirportRepository.findAll().stream()
                .map(flight_Airport -> flight_AirportMapper.toFlight_AirportResponse(flight_Airport,
                        flightRepository, airportRepository))
                .toList();
        return flight_Airports;
    }

    public void deleteAllByFlightId(String flightId) {
        List<Flight_Airport> flight_Airports = flight_AirportRepository.findAllByFlightId(flightId);
        if (flight_Airports.isEmpty()) {
            throw new AppException(ErrorCode.FLIGHT_AIRPORT_NOT_EXISTED);
        }
        flight_AirportRepository.deleteAll(flight_Airports);
    }
}

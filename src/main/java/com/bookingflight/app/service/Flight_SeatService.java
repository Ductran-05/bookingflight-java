package com.bookingflight.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Flight_Seat;
import com.bookingflight.app.dto.request.Flight_SeatRequest;
import com.bookingflight.app.dto.response.Flight_SeatResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
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

    public List<Flight_SeatResponse> getAllFlight_Seats() {
        List<Flight_SeatResponse> flight_Seats = flight_SeatRepository.findAll().stream()
                .map(flight_Seat -> flight_SeatMapper.toFlight_SeatResponse(flight_Seat, flightRepository,
                        seatRepository))
                .toList();
        return flight_Seats;
    }

    public Flight_SeatResponse getFlight_SeatById(String id) {
        Flight_Seat flight_Seat = flight_SeatRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_SEAT_NOT_EXISTED));
        return flight_SeatMapper.toFlight_SeatResponse(flight_Seat, flightRepository, seatRepository);
    }

    public Flight_SeatResponse updateFlight_Seat(String id, Flight_SeatRequest request) {
        Flight_Seat flight_Seat = flight_SeatRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_SEAT_NOT_EXISTED));
        flight_SeatMapper.toFlight_Seat(request, flightRepository, seatRepository);
        flight_Seat.setId(id);
        flight_SeatRepository.save(flight_Seat);
        return flight_SeatMapper.toFlight_SeatResponse(flight_Seat, flightRepository, seatRepository);
    }

    public void deleteFlight_Seat(String id) {
        Flight_Seat flight_Seat = flight_SeatRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_SEAT_NOT_EXISTED));
        flight_SeatRepository.delete(flight_Seat);
    }

    public void deleteAllByFlightId(String flightId) {
        List<Flight_Seat> flight_Seats = flight_SeatRepository.findAllByFlightId(flightId);
        if (flight_Seats.isEmpty()) {
            throw new AppException(ErrorCode.FLIGHT_SEAT_NOT_EXISTED);
        }
        flight_SeatRepository.deleteAll(flight_Seats);
    }
}
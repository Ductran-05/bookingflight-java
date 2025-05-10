package com.bookingflight.app.mapper;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.dto.request.Flight_AirportRequest;
import com.bookingflight.app.dto.response.Flight_AirportResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.AirportRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight_AirportMapper {
        final AirportRepository airportRepository;

        public Flight_Airport toFlight_Airport(Flight_AirportRequest request, Flight flight) {
                return Flight_Airport.builder()
                                .airport(airportRepository.findById(request.getAirportId())
                                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)))
                                .flight(flight)
                                .departureTime(request.getDepartureTime())
                                .arrivalTime(request.getArrivalTime())
                                .note(request.getNote())
                                .build();
        }

        public Flight_AirportResponse toFlight_AirportResponse(Flight_Airport flight_Airport) {
                return Flight_AirportResponse.builder()
                                .id(flight_Airport.getId())
                                .flightId(flight_Airport.getFlight().getId())
                                .airportId(flight_Airport.getAirport().getId())
                                .airportName(flight_Airport.getAirport().getAirportName())
                                .departureTime(flight_Airport.getDepartureTime())
                                .arrivalTime(flight_Airport.getArrivalTime())
                                .note(flight_Airport.getNote())
                                .build();
        }
}
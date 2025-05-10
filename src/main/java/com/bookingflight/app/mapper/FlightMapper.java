package com.bookingflight.app.mapper;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.dto.request.FlightRequest;
import com.bookingflight.app.dto.response.FlightResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.AirportRepository;
import com.bookingflight.app.repository.Flight_AirportRepository;
import com.bookingflight.app.repository.Flight_SeatRepository;
import com.bookingflight.app.repository.PlaneRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightMapper {

        final Flight_AirportRepository flight_AirportRepository;
        final Flight_SeatRepository flight_SeatRepository;
        final PlaneRepository planeRepository;
        final AirportRepository airportRepository;
        final Flight_AirportMapper flight_AirportMapper;
        final Flight_SeatMapper flight_SeatMapper;

        public Flight toFlight(FlightRequest request) {
                return Flight.builder()
                                .flightCode(request.getFlightCode())
                                .plane(planeRepository.findById(request.getPlaneId())
                                                .orElseThrow(() -> new AppException(ErrorCode.PLANE_NOT_EXISTED)))
                                .departureAirport(airportRepository.findById(request.getDepartureAirportId())
                                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)))
                                .arrivalAirport(airportRepository.findById(request.getArrivalAirportId())
                                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)))
                                .arrivalTime(request.getArrivalTime())
                                .departureTime(request.getDepartureTime())
                                .originPrice(request.getOriginPrice())
                                .build();
        }

        public FlightResponse toFlightResponse(Flight flight) {
                return FlightResponse.builder()
                                .id(flight.getId())
                                .flightCode(flight.getFlightCode())
                                .planeId(flight.getPlane().getId())
                                .planeName(flight.getPlane().getPlaneName())
                                .departureAirportId(flight.getDepartureAirport().getId())
                                .arrivalAirportId(flight.getArrivalAirport().getId())
                                .departureAirportName(flight.getDepartureAirport().getAirportName())
                                .arrivalAirportName(flight.getArrivalAirport().getAirportName())
                                .departureTime(flight.getDepartureTime())
                                .arrivalTime(flight.getArrivalTime())
                                .originPrice(flight.getOriginPrice())
                                .listFlight_AirportResponses(flight_AirportRepository.findAllByFlightId(flight.getId())
                                                .stream()
                                                .map(flight_Airport -> flight_AirportMapper
                                                                .toFlight_AirportResponse(flight_Airport))
                                                .toList())
                                .listFlight_SeatResponses(
                                                flight_SeatRepository.findAllByFlightId(flight.getId()).stream()
                                                                .map(flight_Seat -> flight_SeatMapper
                                                                                .toFlight_SeatResponse(flight_Seat))
                                                                .toList())
                                .build();
        }

        public Flight updateFlight(String id, FlightRequest request) {
                return Flight.builder()
                                .id(id)
                                .flightCode(request.getFlightCode())
                                .plane(planeRepository.findById(request.getPlaneId())
                                                .orElseThrow(() -> new AppException(ErrorCode.PLANE_NOT_EXISTED)))
                                .departureAirport(airportRepository.findById(request.getDepartureAirportId())
                                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)))
                                .arrivalAirport(airportRepository.findById(request.getArrivalAirportId())
                                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)))
                                .arrivalTime(request.getArrivalTime())
                                .departureTime(request.getDepartureTime())
                                .originPrice(request.getOriginPrice())
                                .build();

        }
}

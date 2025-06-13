package com.bookingflight.app.mapper;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.FlightStatus;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.dto.request.FlightRequest;
import com.bookingflight.app.dto.response.FlightResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightMapper {

        final TicketRepository ticketRepository;

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
                FlightStatus flightStatus = FlightStatus.AVAILABLE;
                // kiểm tra thời gian đến có trước thời điểm hiện tại không
                if (flight.getArrivalTime().isBefore(LocalDateTime.now())) {
                        flightStatus = FlightStatus.FLOWN;
                }

                // kiểm tra nếu hết vé thì flightStatus = SOLD_OUT
                List<Ticket> tickets = ticketRepository.findAllByFlightId(flight.getId());
                boolean isSoldOut = tickets.stream().allMatch(Ticket::getIsBooked);

                if (isSoldOut) {
                        flightStatus = FlightStatus.SOLD_OUT;
                }

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
                                .flightStatus(flightStatus)
                                .listFlight_Airport(flight_AirportRepository.findAllByFlightId(flight.getId())
                                                .stream()
                                                .map(flight_Airport -> flight_AirportMapper
                                                                .toFlight_AirportResponse(flight_Airport))
                                                .toList())
                                .listFlight_Seat(
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

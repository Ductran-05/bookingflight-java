package com.bookingflight.app.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Flight_Seat;
import com.bookingflight.app.dto.request.Flight_SeatRequest;
import com.bookingflight.app.dto.response.Flight_SeatResponse;
import com.bookingflight.app.repository.SeatRepository;
import com.bookingflight.app.repository.TicketRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight_SeatMapper {

        final TicketRepository ticketRepository;
        final SeatRepository seatRepository;

        public Flight_Seat toFlight_Seat(Flight_SeatRequest request, Flight flight) {
                Flight_Seat flight_Seat = Flight_Seat.builder()
                                .flight(flight)
                                .seat(seatRepository.findById(request.getSeatId()).get())
                                .quantity(request.getQuantity())
                                .build();
                return flight_Seat;
        }

        public Flight_SeatResponse toFlight_SeatResponse(Flight_Seat flight_Seat) {
                Integer quantityBooked = ticketRepository.countByFlightIdAndSeatIdAndIsBookedTrue(
                                flight_Seat.getFlight().getId(), flight_Seat.getSeat().getId());
                Integer quantityAvailable = flight_Seat.getQuantity().intValue() - quantityBooked.intValue();
                return Flight_SeatResponse.builder()
                                .id(flight_Seat.getId())
                                .flightId(flight_Seat.getFlight().getId())
                                .seatId(flight_Seat.getSeat().getId())
                                .seatName(flight_Seat.getSeat().getSeatName())
                                .price(flight_Seat.getSeat().getPrice())
                                .quantity(flight_Seat.getQuantity())
                                .quantityBooked(quantityBooked)
                                .quantityAvailable(quantityAvailable)
                                .build();
        }

}
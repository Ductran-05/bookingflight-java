package com.bookingflight.app.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.dto.request.TicketRequest;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.SeatRepository;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketMapper {
    final FlightRepository flightRepository;
    final SeatRepository seatRepository;

    public Ticket toTicket(TicketRequest request) {
        Ticket ticket = Ticket.builder()
                .flight(flightRepository.findById(request.getFlightId()).get())
                .seat(seatRepository.findById(request.getSeatId()).get())
                .passengerName(request.getPassengerName())
                .passengerPhone(request.getPassengerPhone())
                .passengerIDCard(request.getPassengerIDCard())
                .passengerEmail(request.getPassengerEmail())
                .haveBaggage(request.getHaveBaggage())
                .build();
        return ticket;
    }

    public TicketResponse toTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .flightId(ticket.getFlight().getId())
                .flightCode(ticket.getFlight().getFlightCode())
                .seatId(ticket.getSeat().getId())
                .seatName(ticket.getSeat().getSeatName())
                .passengerName(ticket.getPassengerName())
                .passengerPhone(ticket.getPassengerPhone())
                .passengerIDCard(ticket.getPassengerIDCard())
                .passengerEmail(ticket.getPassengerEmail())
                .haveBaggage(ticket.getHaveBaggage())
                .build();
    }

    public void updateTicket(TicketRequest request, Ticket ticket) {
        ticket.setFlight(flightRepository.findById(request.getFlightId()).get());
        ticket.setSeat(seatRepository.findById(request.getSeatId()).get());
        ticket.setPassengerName(request.getPassengerName());
        ticket.setPassengerPhone(request.getPassengerPhone());
        ticket.setPassengerIDCard(request.getPassengerIDCard());
        ticket.setPassengerEmail(request.getPassengerEmail());
        ticket.setHaveBaggage(request.getHaveBaggage());
    }
}
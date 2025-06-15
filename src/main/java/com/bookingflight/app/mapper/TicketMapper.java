package com.bookingflight.app.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.domain.TicketStatus;
import com.bookingflight.app.dto.request.TicketRequest;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.SeatRepository;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketMapper {
    final FlightRepository flightRepository;
    final SeatRepository seatRepository;
    final AccountRepository accountRepository;

    public Ticket toTicket(TicketRequest request) {
        Ticket ticket = Ticket.builder()
                .account(accountRepository.findById(request.getAccountId()).get())
                .flight(flightRepository.findById(request.getFlightId()).get())
                .seat(seatRepository.findById(request.getSeatId()).get())
                .passengerName(request.getPassengerName())
                .passengerPhone(request.getPassengerPhone())
                .passengerIDCard(request.getPassengerIDCard())
                .passengerEmail(request.getPassengerEmail())
                .urlImage(request.getUrlImage())
                .haveBaggage(request.getHaveBaggage())
                .build();
        return ticket;
    }

    public TicketResponse toTicketResponse(Ticket ticket) {
        TicketStatus ticketStatus = ticket.getTicketStatus();
        if (!ticketStatus.equals(TicketStatus.CANCELLED)) {
            // nếu thời gian hiện tại > thời gian bay + 2 giờ và bé hơn thời gian
            // tới thì đặt là boarding
            if (ticket.getFlight().getDepartureTime().isBefore(LocalDateTime.now().plusHours(2))) {
                if (ticket.getFlight().getArrivalTime().isAfter(LocalDateTime.now())) {
                    ticket.setTicketStatus(TicketStatus.BOARDING);
                }
                ticket.setTicketStatus(TicketStatus.USED);
            }

        }
        LocalDateTime pickupAt = ticket.getPickupAt();
        if (pickupAt != null) {
            if (pickupAt.isBefore(LocalDateTime.now().minusMinutes(5))) {
                pickupAt = null;
            }
        }
        Boolean canBook = ticket.getTicketStatus().equals(TicketStatus.AVAILABLE) && ticket.getPickupAt() == null;
        return TicketResponse.builder()
                .id(ticket.getId())
                .flight(ticket.getFlight())
                .seatId(ticket.getSeat().getId())
                .seatName(ticket.getSeat().getSeatName())
                .passengerName(ticket.getPassengerName())
                .passengerPhone(ticket.getPassengerPhone())
                .passengerIDCard(ticket.getPassengerIDCard())
                .passengerEmail(ticket.getPassengerEmail())
                .haveBaggage(ticket.getHaveBaggage())
                .seatNumber(ticket.getSeatNumber())
                .canBook(canBook)
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
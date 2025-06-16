package com.bookingflight.app.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

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
        return Ticket.builder()
                .account(accountRepository.findById(request.getAccountId()).orElse(null))
                .flight(flightRepository.findById(request.getFlightId()).orElse(null))
                .seat(seatRepository.findById(request.getSeatId()).orElse(null))
                .passengerName(request.getPassengerName())
                .passengerPhone(request.getPassengerPhone())
                .passengerIDCard(request.getPassengerIDCard())
                .passengerEmail(request.getPassengerEmail())
                .urlImage(request.getUrlImage())
                .haveBaggage(request.getHaveBaggage())
                .build();
    }

    public TicketResponse toTicketResponse(Ticket ticket) {
        LocalDateTime now = LocalDateTime.now();

        // Cập nhật trạng thái nếu chưa bị CANCELLED
        if (!ticket.getTicketStatus().equals(TicketStatus.CANCELLED)) {
            LocalDateTime departureTime = ticket.getFlight().getDepartureTime();
            LocalDateTime arrivalTime = ticket.getFlight().getArrivalTime();

            if (departureTime != null && arrivalTime != null) {
                if (now.isAfter(departureTime.minusHours(2)) && now.isBefore(arrivalTime)) {
                    ticket.setTicketStatus(TicketStatus.BOARDING);
                } else if (now.isAfter(arrivalTime)) {
                    ticket.setTicketStatus(TicketStatus.USED);
                }
            }
        }

        // Kiểm tra có thể đặt không (vé chưa nhận và đang boarding)
        LocalDateTime pickupAt = ticket.getPickupAt();
        if (pickupAt != null && pickupAt.isBefore(now.minusSeconds(10))) {
            pickupAt = null;
        }

        boolean canBook = pickupAt == null && ticket.getTicketStatus().equals(TicketStatus.AVAILABLE);

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
        ticket.setFlight(flightRepository.findById(request.getFlightId()).orElse(null));
        ticket.setSeat(seatRepository.findById(request.getSeatId()).orElse(null));
        ticket.setPassengerName(request.getPassengerName());
        ticket.setPassengerPhone(request.getPassengerPhone());
        ticket.setPassengerIDCard(request.getPassengerIDCard());
        ticket.setPassengerEmail(request.getPassengerEmail());
        ticket.setHaveBaggage(request.getHaveBaggage());
    }
}

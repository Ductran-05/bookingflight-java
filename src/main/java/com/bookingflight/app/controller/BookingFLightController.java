package com.bookingflight.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookingflight.app.domain.Flight_Seat;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.dto.request.ListTicketRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.TicketMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.SeatRepository;
import com.bookingflight.app.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookingFLightController {
    private final FlightRepository flightRepository;
    private final AccountRepository accountRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @PostMapping("/api/booking-flight")
    public ResponseEntity<APIResponse<List<TicketResponse>>> bookingFlight(@RequestBody ListTicketRequest request) {
        List<TicketResponse> ticketResponses = new java.util.ArrayList<>();
        int remainingSeat = ticketRepository.countByFlightIdAndSeatIdAndIsBookedFalse(request.getFlightId(),
                request.getSeatId());
        if (remainingSeat < request.getPassengers().size()) {
            throw new AppException(ErrorCode.NOT_ENOUGH_SEATS);
        }
        for (ListTicketRequest.Passenger passenger : request.getPassengers()) {
            Ticket unusedTicket = ticketRepository.findFirstByFlightIdAndSeatIdAndIsBookedFalse(request.getFlightId(),
                    request.getSeatId());
            if (unusedTicket == null) {
                throw new AppException(ErrorCode.NOT_ENOUGH_SEATS);
            }
            unusedTicket.setAccount(accountRepository.findById(request.getAccountId()).get());
            unusedTicket.setPassengerName(passenger.getPassengerName());
            unusedTicket.setPassengerPhone(passenger.getPassengerPhone());
            unusedTicket.setPassengerIDCard(passenger.getPassengerIDCard());
            unusedTicket.setPassengerEmail(passenger.getPassengerEmail());
            unusedTicket.setHaveBaggage(passenger.getHaveBaggage());
            unusedTicket.setIsBooked(true);
            Ticket ticket = ticketRepository.save(unusedTicket);
            ticketResponses.add(ticketMapper.toTicketResponse(ticket));
        }
        APIResponse<List<TicketResponse>> apiResponse = APIResponse.<List<TicketResponse>>builder()
                .Code(HttpStatus.OK.value())
                .Message("Booking flight successfully")
                .data(ticketResponses)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}

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

    @PostMapping("/booking-flight")
    public ResponseEntity<APIResponse<List<TicketResponse>>> bookingFlight(@RequestBody ListTicketRequest request) {
        List<TicketResponse> ticketResponses = new java.util.ArrayList<>();
        for (ListTicketRequest.Passenger passenger : request.getPassengers()) {
            Ticket ticket = Ticket.builder()
                    .flight(flightRepository.findById(request.getFlightId()).get())
                    .account(accountRepository.findById(request.getAccountId()).get())
                    .seat(seatRepository.findById(request.getSeatId()).get())
                    .passengerName(passenger.getPassengerName())
                    .passengerPhone(passenger.getPassengerPhone())
                    .passengerIDCard(passenger.getPassengerIDCard())
                    .passengerEmail(passenger.getPassengerEmail())
                    .haveBaggage(passenger.getHaveBaggage())
                    .build();

            ticketRepository.save(ticket);
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

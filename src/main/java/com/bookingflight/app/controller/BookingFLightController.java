package com.bookingflight.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.domain.TicketStatus;
import com.bookingflight.app.dto.request.ListTicketRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.TicketMapper;
import com.bookingflight.app.repository.AccountRepository;
import com.bookingflight.app.repository.TicketRepository;
import com.bookingflight.app.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookingFLightController {
    private final AccountRepository accountRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final EmailService emailService;

    @PostMapping("/api/booking-flight")
    public ResponseEntity<APIResponse<List<TicketResponse>>> bookingFlight(@RequestBody ListTicketRequest request) {
        List<TicketResponse> ticketResponses = new java.util.ArrayList<>();

        for (ListTicketRequest.Passenger passenger : request.getPassengers()) {
            Ticket unusedTicket = ticketRepository.findById(passenger.getTicketId()).get();
            unusedTicket.setAccount(
                    request.getAccountId() == null ? null : accountRepository.findById(request.getAccountId()).get());
            unusedTicket.setPassengerName(passenger.getPassengerName());
            unusedTicket.setPassengerPhone(passenger.getPassengerPhone());
            unusedTicket.setPassengerIDCard(passenger.getPassengerIDCard());
            unusedTicket.setPassengerEmail(passenger.getPassengerEmail());
            unusedTicket.setHaveBaggage(passenger.getHaveBaggage());
            unusedTicket.setUrlImage(passenger.getUrlImage());
            unusedTicket.setTicketStatus(TicketStatus.BOOKED);
            unusedTicket.setIsBooked(true);
            Ticket ticket = ticketRepository.save(unusedTicket);
            ticketResponses.add(ticketMapper.toTicketResponse(ticket));
            // emailService.send(passenger.getPassengerEmail(),
            // buildEmail(ticket.getUrlImage()));
        }
        APIResponse<List<TicketResponse>> apiResponse = APIResponse.<List<TicketResponse>>builder()
                .Code(HttpStatus.OK.value())
                .Message("Booking flight successfully")
                .data(ticketResponses)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private String buildEmail(String link) {
        return "Chào bạn,\n\n"
                + "Cảm ơn bạn đã đặt vé. Vui lòng nhấn vào liên kết dưới đây để xem thông tin vé:\n"
                + link + "\n\n"
                + "Trân trọng.";
    }

    @PostMapping("/api/booking-flight/cancel")
    public ResponseEntity<APIResponse<String>> cancelTicket(@RequestBody List<String> ticketIds) {
        for (String ticketId : ticketIds) {
            Ticket ticket = ticketRepository.findById(ticketId).get();
            ticket.setIsBooked(false);
            ticket.setTicketStatus(TicketStatus.CANCELLED);
            ticketRepository.save(ticket);
        }
        APIResponse<String> apiResponse = APIResponse.<String>builder()
                .Code(HttpStatus.OK.value())
                .Message("Cancel ticket successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}

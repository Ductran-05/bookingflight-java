package com.bookingflight.app.controller;

import com.bookingflight.app.dto.request.TicketRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<APIResponse<List<TicketResponse>>> getAllTickets() {
        List<TicketResponse> tickets = ticketService.getAllTickets();
        APIResponse<List<TicketResponse>> apiResponse = APIResponse.<List<TicketResponse>>builder()
                .Code(HttpStatus.OK.value())
                .Message("Get all tickets successfully")
                .data(tickets)
                .build();
        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<TicketResponse>> getTicketById(@PathVariable String id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        APIResponse<TicketResponse> apiResponse = APIResponse.<TicketResponse>builder()
                .Code(HttpStatus.OK.value())
                .Message("Get ticket by id successfully")
                .data(ticket)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity<APIResponse<TicketResponse>> createTicket(@RequestBody @Valid TicketRequest request) {
        TicketResponse ticket = ticketService.createTicket(request);
        APIResponse<TicketResponse> apiResponse = APIResponse.<TicketResponse>builder()
                .Code(HttpStatus.CREATED.value())
                .Message("Create ticket successfully")
                .data(ticket)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<TicketResponse>> updateTicket(@PathVariable String id,
            @RequestBody TicketRequest request) {
        TicketResponse ticket = ticketService.updateTicket(id, request);
        APIResponse<TicketResponse> apiResponse = APIResponse.<TicketResponse>builder()
                .Code(HttpStatus.OK.value())
                .Message("Update ticket successfully")
                .data(ticket)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteTicket(@PathVariable String id) {
        ticketService.deleteTicket(id);
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .Code(HttpStatus.NO_CONTENT.value())
                .Message("Delete ticket successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
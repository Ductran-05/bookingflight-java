package com.bookingflight.app.controller;

import com.bookingflight.app.domain.Payment;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.domain.TicketStatus;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.PaymentRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.PaymentResponse;
import com.bookingflight.app.dto.response.PaymentUrlResponse;
import com.bookingflight.app.repository.TicketRepository;
import com.bookingflight.app.service.EmailService;
import com.bookingflight.app.service.PaymentService;
import com.bookingflight.app.service.TicketService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final TicketService ticketService;
    private final EmailService emailService;
    private final TicketRepository ticketRepository;

    @GetMapping("/")
    public ResponseEntity<APIResponse<ResultPaginationDTO>> getAllPayments(
            @Filter Specification<Payment> spec,
            Pageable pageable) {
        APIResponse<ResultPaginationDTO> apiResponse = APIResponse.<ResultPaginationDTO>builder()
                .Code(200)
                .Message("Get all payments successfully")
                .data(paymentService.getAllPayments(spec, pageable))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse<PaymentUrlResponse>> createPayment(
            @RequestBody PaymentRequest request,
            HttpServletRequest httpRequest) {
        PaymentUrlResponse response = paymentService.createPaymentUrl(request, httpRequest);
        APIResponse<PaymentUrlResponse> apiResponse = APIResponse.<PaymentUrlResponse>builder()
                .Code(200)
                .Message("Payment URL created successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/vnpay-return")
    public RedirectView vnpayReturn(@RequestParam Map<String, String> params) {
        PaymentResponse response = paymentService.handlePaymentReturn(params);
        String redirectUrl;
        if (response.getStatus() == Payment.PaymentStatus.SUCCESS) {
            // emailService.send(passenger.getPassengerEmail(),
            // buildEmail(ticket.getUrlImage()));
            redirectUrl = "http://localhost:5173/payment/success?tmxRef=" + response.getTxnRef();
        } else {
            redirectUrl = "http://localhost:5173/payment/fail";
            List<String> orders = response.getOrderInfo();

            // Delete tickets for each failed order
            if (orders != null && !orders.isEmpty()) {
                for (String ticketId : orders) {
                    try {
                        Ticket ticket = ticketRepository.findById(ticketId).get();
                        ticket.setIsBooked(false);
                        ticket.setTicketStatus(TicketStatus.AVAILABLE);
                        ticketRepository.save(ticket);
                    } catch (Exception e) {
                        System.err
                                .println("Failed to delete ticket with ID: " + ticketId + ", Error: " + e.getMessage());
                    }
                }
            }
        }
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/status/{txnRef}")
    public ResponseEntity<APIResponse<PaymentResponse>> getPaymentStatus(@PathVariable String txnRef) {
        PaymentResponse response = paymentService.getPaymentByTxnRef(txnRef);
        APIResponse<PaymentResponse> apiResponse = APIResponse.<PaymentResponse>builder()
                .Code(200)
                .Message("Payment status retrieved successfully")
                .data(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
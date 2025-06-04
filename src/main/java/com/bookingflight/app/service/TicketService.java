package com.bookingflight.app.service;

import com.bookingflight.app.domain.Account;
import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.TicketRequest;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.mapper.TicketMapper;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.SeatRepository;
import com.bookingflight.app.repository.TicketRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final ResultPanigationMapper resultPanigationMapper;

    public ResultPaginationDTO getAllTickets(Specification<Ticket> spec, Pageable pageable) {
        Page<TicketResponse> page = ticketRepository.findAll(spec, pageable)
                .map(ticketMapper::toTicketResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public TicketResponse createTicket(TicketRequest request) {
        flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_FOUND));
        seatRepository.findById(request.getSeatId()).orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_EXISTED));
        Ticket ticket = ticketMapper.toTicket(request);
        ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }

    public TicketResponse getTicketById(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        return ticketMapper.toTicketResponse(ticket);
    }

    @Transactional
    public TicketResponse updateTicket(String id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_FOUND));
        seatRepository.findById(request.getSeatId()).orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_EXISTED));
        ticketMapper.updateTicket(request, ticket);
        ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }

    @Transactional
    public void deleteTicket(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));

        ticketRepository.delete(ticket);
    }

}

// @Transactional
// public APIResponse<TicketResponse> createTicket(TicketRequest ticketRequest)
// {
// Ticket ticket = ticketMapper.toTicket(ticketRequest);

// Flight flight = flightRepository.findById(ticketRequest.getFlightId())
// .orElseThrow(() -> new RuntimeException("Flight not found with id: " +
// ticketRequest.getFlightId()));

// Seat seat = seatRepository.findById(ticketRequest.getSeatId())
// .orElseThrow(() -> new RuntimeException("Seat not found with id: " +
// ticketRequest.getSeatId()));

// if (isSeatAlreadyBooked(flight.getId(), seat.getId())) {
// throw new RuntimeException("Seat " + seat.getId() + " is already booked for
// flight " + flight.getId());
// }

// ticket.setFlight(flight);
// ticket.setSeat(seat);

// Ticket savedTicket = ticketRepository.save(ticket);

// return ticketMapper.toTicketResponse(savedTicket);
// }

// public APIResponse<TicketResponse> getTicketById(String id) {
// Ticket ticket = ticketRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

// return ticketMapper.toTicketResponse(ticket);
// }

// public List<APIResponse<TicketResponse>> getAllTickets() {
// List<Ticket> tickets = ticketRepository.findAll();

// return tickets.stream()
// .map(ticketMapper::toTicketResponse)
// .collect(Collectors.toList());
// }

// @Transactional
// public APIResponse<TicketResponse> updateTicket(String id, TicketRequest
// ticketRequest) {
// Ticket existingTicket = ticketRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

// if (!existingTicket.getFlight().getId().equals(ticketRequest.getFlightId())
// ||
// !existingTicket.getSeat().getId().equals(ticketRequest.getSeatId())) {

// Flight flight = flightRepository.findById(ticketRequest.getFlightId())
// .orElseThrow(() -> new RuntimeException("Flight not found with id: " +
// ticketRequest.getFlightId()));

// Seat seat = seatRepository.findById(ticketRequest.getSeatId())
// .orElseThrow(() -> new RuntimeException("Seat not found with id: " +
// ticketRequest.getSeatId()));

// if (isSeatAlreadyBookedExcludingTicket(flight.getId(), seat.getId(), id)) {
// throw new RuntimeException("Seat " + seat.getId() + " is already booked for
// flight " + flight.getId());
// }

// existingTicket.setFlight(flight);
// existingTicket.setSeat(seat);
// }

// existingTicket.setPassengerName(ticketRequest.getPassengerName());
// existingTicket.setPassengerPhone(ticketRequest.getPassengerPhone());
// existingTicket.setPassengerIDCard(ticketRequest.getPassengerIDCard());
// existingTicket.setPassengerEmail(ticketRequest.getPassengerEmail());

// Ticket updatedTicket = ticketRepository.save(existingTicket);

// return ticketMapper.toTicketResponse(updatedTicket);
// }

// @Transactional
// public void deleteTicket(String id) {
// if (!ticketRepository.existsById(id)) {
// throw new RuntimeException("Ticket not found with id: " + id);
// }

// ticketRepository.deleteById(id);
// }

// private boolean isSeatAlreadyBooked(String flightId, String seatId) {
// return ticketRepository.findAll().stream()
// .anyMatch(ticket -> ticket.getFlight().getId().equals(flightId) &&
// ticket.getSeat().getId().equals(seatId));
// }

// private boolean isSeatAlreadyBookedExcludingTicket(String flightId, String
// seatId, String ticketId) {
// return ticketRepository.findAll().stream()
// .anyMatch(ticket -> !ticket.getId().equals(ticketId) &&
// ticket.getFlight().getId().equals(flightId) &&
// ticket.getSeat().getId().equals(seatId));
// }
// }
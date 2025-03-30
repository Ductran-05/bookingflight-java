package com.bookingflight.app.service;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Seat;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.dto.request.TicketRequest;
import com.bookingflight.app.dto.response.TicketResponse;
import com.bookingflight.app.mapper.TicketMapper;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.SeatRepository;
import com.bookingflight.app.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public TicketResponse createTicket(TicketRequest ticketRequest) {
        Ticket ticket = ticketMapper.toTicket(ticketRequest);

        Flight flight = flightRepository.findById(ticketRequest.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + ticketRequest.getFlightId()));

        Seat seat = seatRepository.findById(ticketRequest.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + ticketRequest.getSeatId()));

        if (isSeatAlreadyBooked(flight.getId(), seat.getId())) {
            throw new RuntimeException("Seat " + seat.getId() + " is already booked for flight " + flight.getId());
        }

        ticket.setFlight(flight);
        ticket.setSeat(seat);

        Ticket savedTicket = ticketRepository.save(ticket);

        return ticketMapper.toTicketResponse(savedTicket);
    }

    public TicketResponse getTicketById(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        return ticketMapper.toTicketResponse(ticket);
    }

    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        return tickets.stream()
                .map(ticketMapper::toTicketResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponse updateTicket(String id, TicketRequest ticketRequest) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        if (!existingTicket.getFlight().getId().equals(ticketRequest.getFlightId()) ||
                !existingTicket.getSeat().getId().equals(ticketRequest.getSeatId())) {

            Flight flight = flightRepository.findById(ticketRequest.getFlightId())
                    .orElseThrow(() -> new RuntimeException("Flight not found with id: " + ticketRequest.getFlightId()));

            Seat seat = seatRepository.findById(ticketRequest.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found with id: " + ticketRequest.getSeatId()));

            if (isSeatAlreadyBookedExcludingTicket(flight.getId(), seat.getId(), id)) {
                throw new RuntimeException("Seat " + seat.getId() + " is already booked for flight " + flight.getId());
            }

            existingTicket.setFlight(flight);
            existingTicket.setSeat(seat);
        }

        existingTicket.setPassengerName(ticketRequest.getPassengerName());
        existingTicket.setPassengerPhone(ticketRequest.getPassengerPhone());
        existingTicket.setPassengerIDCard(ticketRequest.getPassengerIDCard());
        existingTicket.setPassengerEmail(ticketRequest.getPassengerEmail());

        Ticket updatedTicket = ticketRepository.save(existingTicket);

        return ticketMapper.toTicketResponse(updatedTicket);
    }

    @Transactional
    public void deleteTicket(String id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket not found with id: " + id);
        }

        ticketRepository.deleteById(id);
    }

    private boolean isSeatAlreadyBooked(String flightId, String seatId) {
        return ticketRepository.findAll().stream()
                .anyMatch(ticket -> ticket.getFlight().getId().equals(flightId) &&
                        ticket.getSeat().getId().equals(seatId));
    }

    private boolean isSeatAlreadyBookedExcludingTicket(String flightId, String seatId, String ticketId) {
        return ticketRepository.findAll().stream()
                .anyMatch(ticket -> !ticket.getId().equals(ticketId) &&
                        ticket.getFlight().getId().equals(flightId) &&
                        ticket.getSeat().getId().equals(seatId));
    }
}
package com.bookingflight.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.domain.Flight_Seat;
import com.bookingflight.app.domain.Ticket;
import com.bookingflight.app.domain.TicketStatus;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.FlightRequest;
import com.bookingflight.app.dto.request.Flight_AirportRequest;
import com.bookingflight.app.dto.request.Flight_SeatRequest;
import com.bookingflight.app.dto.response.FlightResponse;
import com.bookingflight.app.dto.response.SeatResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.FlightMapper;
import com.bookingflight.app.mapper.Flight_AirportMapper;
import com.bookingflight.app.mapper.Flight_SeatMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.mapper.SeatMapper;
import com.bookingflight.app.mapper.TicketMapper;
import com.bookingflight.app.repository.*;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class FlightService {
        private final ResultPanigationMapper resultPanigationMapper;

        final FlightRepository flightRepository;
        final FlightMapper flightMapper;
        final Flight_AirportRepository flight_AirportRepository;
        final Flight_AirportMapper flight_AirportMapper;
        final Flight_SeatRepository flight_SeatRepository;
        final Flight_SeatMapper flight_SeatMapper;
        final SeatMapper seatMapper;
        final SeatRepository seatRepository;
        final TicketRepository ticketRepository;
        final TicketMapper ticketMapper;
        final EmailService emailService;

        public ResultPaginationDTO getAllFlights(Specification<Flight> spec, Pageable pageable) {
                Page<FlightResponse> page = flightRepository.findAll(spec, pageable)
                                .map(flightMapper::toFlightResponse);
                return resultPanigationMapper.toResultPanigationMapper(page);
        }

        public FlightResponse createFlight(FlightRequest request) {

                Flight flight = flightMapper.toFlight(request);
                flightRepository.findByFlightCode(request.getFlightCode()).ifPresent(flight1 -> {
                        throw new AppException(ErrorCode.FLIGHT_EXISTED);
                });
                flightRepository.save(flight);
                if (request.getListFlight_Airport() != null) {
                        for (Flight_AirportRequest flight_AirportRequest : request.getListFlight_Airport()) {
                                Flight_Airport flight_Airport = flight_AirportMapper.toFlight_Airport(
                                                flight_AirportRequest,
                                                flight);
                                flight_AirportRepository.save(flight_Airport);
                        }
                }

                if (request.getListFlight_Seat() != null) {
                        for (Flight_SeatRequest flight_SeatRequest : request.getListFlight_Seat()) {
                                Flight_Seat flight_Seat = flight_SeatMapper.toFlight_Seat(flight_SeatRequest, flight);
                                flight_SeatRepository.save(flight_Seat);
                        }
                        // Tạo các vé trống cho chuyến bay
                        for (Flight_Seat flight_seat : flight_SeatRepository.findAllByFlightId(flight.getId())) {
                                int quantity = flight_seat.getQuantity().intValue();
                                for (int i = 0; i < quantity; i++) {
                                        Ticket ticket = Ticket.builder()
                                                        .flight(flight)
                                                        .seat(flight_seat.getSeat())
                                                        .passengerName("")
                                                        .passengerPhone("")
                                                        .passengerIDCard("")
                                                        .passengerEmail("")
                                                        .haveBaggage(false)
                                                        .ticketStatus(TicketStatus.AVAILABLE)
                                                        .seatNumber(i + 1)
                                                        .build();
                                        ticketRepository.save(ticket);
                                }
                        }

                }

                return flightMapper.toFlightResponse(flight);
        }

        public FlightResponse getFlightById(String id) throws AppException {
                Flight flight = flightRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_FOUND));
                return flightMapper.toFlightResponse(flight);
        }

        @Transactional
        public FlightResponse updateFlight(String id, FlightRequest request) throws AppException {
                Flight flight = flightRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_FOUND));
                // flight_SeatRepository.deleteAllByFlightId(id);
                flight_AirportRepository.deleteAllByFlightId(id);

                flight = flightMapper.updateFlight(id, request);
                flightRepository.save(flight);

                for (Flight_AirportRequest flight_AirportRequest : request.getListFlight_Airport()) {
                        Flight_Airport flight_Airport = flight_AirportMapper.toFlight_Airport(flight_AirportRequest,
                                        flight);
                        flight_AirportRepository.save(flight_Airport);
                }

                // for (Flight_SeatRequest flight_SeatRequest : request.getListFlight_Seat()) {
                // Flight_Seat flight_Seat = flight_SeatMapper.toFlight_Seat(flight_SeatRequest,
                // flight);
                // flight_SeatRepository.save(flight_Seat);
                // }
                for (Ticket ticket : ticketRepository.findAllByFlightId(id)) {

                        // gửi mail cho khach hang bao cap nhat chuyen bay
                        if (!ticket.getPassengerEmail().equals("")) {
                                emailService.send(ticket.getPassengerEmail(),
                                                buildUpdateEmail("http://localhost:5173/ticketSearchPage", ticket));
                        }

                }
                return flightMapper.toFlightResponse(flight);
        }

        @Transactional
        public void deleteFlight(String id) throws AppException {

                Flight flight = flightRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_FOUND));

                for (Ticket ticket : ticketRepository.findAllByFlightId(id)) {
                        ticket.setTicketStatus(TicketStatus.CANCELLED);
                        ticket.setFlight(null);
                        ticketRepository.save(ticket);
                        if (!ticket.getPassengerEmail().equals("")) {
                                emailService.send(ticket.getPassengerEmail(),
                                                buildEmail("http://localhost:5173/ticketSearchPage", ticket));
                        }

                        // gửi mail cho khach hang bao cap nhat chuyen bay

                }
                flight_SeatRepository.deleteAllByFlightId(id);
                flight_AirportRepository.deleteAllByFlightId(id);
                flightRepository.delete(flight);
        }

        private String buildEmail(String link, Ticket ticket) {
                return "Dear " + ticket.getPassengerName() + ",\n\n"
                                + "We regret to inform you that your flight has been cancelled.\n\n"
                                + "Here are your ticket details:\n"
                                + "----------------------------------------\n"
                                + "Passenger Name: " + ticket.getPassengerName() + "\n"
                                + "Phone Number: " + ticket.getPassengerPhone() + "\n"
                                + "Email: " + ticket.getPassengerEmail() + "\n"
                                + "Seat Number: " + ticket.getSeatNumber() + "\n"
                                + "Baggage: " + (ticket.getHaveBaggage() ? "Yes" : "No") + "\n"
                                + "Ticket Status: " + ticket.getTicketStatus() + "\n"
                                + "----------------------------------------\n\n"
                                + "You can still access your ticket image for reference by clicking the link below:\n"
                                + link + "\n\n"
                                + "We apologize for any inconvenience this may have caused.\n"
                                + "If you need further assistance, please contact our customer support.\n\n"
                                + "Best regards.";
        }

        private String buildUpdateEmail(String link, Ticket ticket) {
                return "Dear " + ticket.getPassengerName() + ",\n\n"
                                + "Your ticket information has been successfully updated.\n\n"
                                + "Here are your updated ticket details:\n"
                                + "----------------------------------------\n"
                                + "Flight Code: " + ticket.getFlight().getFlightCode() + "\n"
                                + "Passenger Name: " + ticket.getPassengerName() + "\n"
                                + "Phone Number: " + ticket.getPassengerPhone() + "\n"
                                + "Email: " + ticket.getPassengerEmail() + "\n"
                                + "Seat Number: " + ticket.getSeatNumber() + "\n"
                                + "Baggage: " + (ticket.getHaveBaggage() ? "Yes" : "No") + "\n"
                                + "----------------------------------------\n\n"
                                + "You can view or download your updated ticket image by clicking the link below:\n"
                                + link + "\n\n"
                                + "If you have any further questions, please contact our customer support.\n\n"
                                + "Best regards.";
        }

        private boolean canDeleteFlight(String id) {
                for (Ticket ticket : ticketRepository.findAllByFlightId(id)) {
                        if (ticket.getTicketStatus() != TicketStatus.AVAILABLE)
                                return false;
                }
                return true;
        }

        public List<SeatResponse> getSeatsByFlightId(String id) {
                return flight_SeatRepository.findAllByFlightId(id).stream()
                                .map(flight_Seat -> seatMapper.toSeatResponse(flight_Seat.getSeat()))
                                .toList();
        }

}

package com.bookingflight.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.domain.Flight_Seat;
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
                flight_SeatRepository.deleteAllByFlightId(id);
                flight_AirportRepository.deleteAllByFlightId(id);

                flight = flightMapper.updateFlight(id, request);
                flightRepository.save(flight);

                for (Flight_AirportRequest flight_AirportRequest : request.getListFlight_Airport()) {
                        Flight_Airport flight_Airport = flight_AirportMapper.toFlight_Airport(flight_AirportRequest,
                                        flight);
                        flight_AirportRepository.save(flight_Airport);
                }

                for (Flight_SeatRequest flight_SeatRequest : request.getListFlight_Seat()) {
                        Flight_Seat flight_Seat = flight_SeatMapper.toFlight_Seat(flight_SeatRequest, flight);
                        flight_SeatRepository.save(flight_Seat);
                }
                return flightMapper.toFlightResponse(flight);
        }

        @Transactional
        public void deleteFlight(String id) throws AppException {

                Flight flight = flightRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_FOUND));
                flight_SeatRepository.deleteAllByFlightId(id);
                flight_AirportRepository.deleteAllByFlightId(id);
                flightRepository.delete(flight);
        }

        public List<SeatResponse> getSeatsByFlightId(String id) {
                return flight_SeatRepository.findAllByFlightId(id).stream()
                                .map(flight_Seat -> seatMapper.toSeatResponse(flight_Seat.getSeat()))
                                .toList();
        }
}

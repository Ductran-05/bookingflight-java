package com.bookingflight.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.domain.Plane;
import com.bookingflight.app.dto.request.FlightRequest;
import com.bookingflight.app.dto.request.Flight_AirportRequest;
import com.bookingflight.app.dto.request.Flight_SeatRequest;
import com.bookingflight.app.dto.response.FlightResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.FlightMapper;
import com.bookingflight.app.mapper.Flight_AirportMapper;
import com.bookingflight.app.mapper.Flight_SeatMapper;
import com.bookingflight.app.repository.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@AllArgsConstructor
public class FlightService {

        private final Flight_AirportRepository flight_AirportRepository;
        private final FlightMapper flightMapper;
        private final FlightRepository flightRepository;
        private final PlaneRepository planeRepository;
        private final AirportRepository airportRepository;
        private final Flight_AirportService flight_AirportService;
        private final Flight_SeatService flight_SeatService;
        private final Flight_AirportMapper flight_AirportMapper;
        private final Flight_SeatRepository flight_SeatRepository;
        private final SeatRepository seatRepository;
        private final Flight_SeatMapper flight_SeatMapper;

        public FlightResponse createFlight(FlightRequest request) {
                Plane plane = planeRepository.findById(request.getPlaneId())
                                .orElseThrow(() -> new AppException(ErrorCode.PLANE_NOT_EXISTED));
                Airport departureAirport = airportRepository.findById(request.getDepartureAirportId())
                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED));
                Airport arrivalAirport = airportRepository.findById(request.getArrivalAirportId())
                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED));
                flightRepository.findByFlightCode(request.getFlightCode())
                                .ifPresent(flight -> {
                                        throw new AppException(ErrorCode.FLIGHT_EXISTED);
                                });

                Flight flight = flightMapper.toFlight(request, planeRepository, airportRepository);
                flightRepository.save(flight);

                for (Flight_AirportRequest flight_AirportRequest : request.getIntermediateAirports()) {
                        flight_AirportRequest.setFlightId(flight.getId());
                        flight_AirportService.createFlight_Airport(flight_AirportRequest);
                }

                for (Flight_SeatRequest flight_SeatRequest : request.getListFlight_Seat()) {
                        flight_SeatRequest.setFlightId(flight.getId());
                        flight_SeatService.createFlight_Seat(flight_SeatRequest);
                }

                return toResponse(flight);
        }

        public List<FlightResponse> getAllFlights() {
                List<Flight> flights = flightRepository.findAll();
                return flights.stream().map(t -> toResponse(t)).toList();
        }

        public FlightResponse getFlightById(String id) {
                Flight flight = flightRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));
                return toResponse(flight);
        }

        public FlightResponse updateFlight(String id, FlightRequest request) {
                Flight flight = flightRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));
                flight = flightMapper.toFlight(request, planeRepository, airportRepository);
                flight.setId(id);
                flightRepository.save(flight);
                return toResponse(flight);
        }

        public void deleteFlight(String id) {
                Flight flight = flightRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED));
                flight_AirportService.deleteAllByFlightId(id);
                flight_SeatService.deleteAllByFlightId(id);
                flightRepository.delete(flight);
        }

        public FlightResponse toResponse(Flight flight) {
                FlightResponse flightResponse = flightMapper.toFlightResponse(flight);
                flightResponse.setIntermediateAirports(flight_AirportRepository.findAllByFlightId(flight.getId())
                                .stream()
                                .map(flight_Airport -> flight_AirportMapper.toFlight_AirportResponse(flight_Airport,
                                                flightRepository,
                                                airportRepository))
                                .toList());
                flightResponse.setListFlight_SeatResponses(
                                flight_SeatRepository.getAllFlight_SeatByFlightId(flight.getId())
                                                .stream()
                                                .map(flight_Seat -> flight_SeatMapper.toFlight_SeatResponse(flight_Seat,
                                                                flightRepository,
                                                                seatRepository))
                                                .toList());
                return flightResponse;
        }
}
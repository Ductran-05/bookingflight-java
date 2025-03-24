// package com.bookingflight.app.service;

// import org.springframework.stereotype.Service;

// import com.bookingflight.app.domain.Flight;
// import com.bookingflight.app.domain.Plane;
// import com.bookingflight.app.dto.request.FlightRequest;
// import com.bookingflight.app.dto.response.FlightResponse;
// import com.bookingflight.app.mapper.FlightMapper;
// import com.bookingflight.app.repository.FlightRepository;
// import com.bookingflight.app.repository.PlaneRepository;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class FlightService {
// private final FlightMapper flightMapper;
// private final FlightRepository flightRepository;
// private final PlaneRepository planeRepository;
// private final AirportRepository airportRepository;

// public FlightResponse createFlight(FlightRequest request) {
// if(planeRepository.findById(request.getPlaneId())!=null
// || ) {

// }
// Flight flight = flightMapper.toFlight(request);
// flightRepository.save(flight);

// return flightMapper.toFlightResponse(flight);
// }
// }

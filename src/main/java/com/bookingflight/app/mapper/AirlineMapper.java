package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Airline;
import com.bookingflight.app.dto.request.AirlineRequest;
import com.bookingflight.app.dto.response.AirlineResponse;
import com.bookingflight.app.repository.FlightRepository;
import com.bookingflight.app.repository.PlaneRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class AirlineMapper {
    private final FlightRepository flightRepository;
    private final PlaneRepository planeRepository;

    public Airline toAirline(AirlineRequest request) {
        Airline airline = Airline.builder()
                .airlineCode(request.getAirlineCode())
                .airlineName(request.getAirlineName())
                .build();
        return airline;
    }

    public AirlineResponse toAirlineResponse(Airline airline) {
        Boolean canUpdate = airline.getCanUpdate();
        Boolean canDelete = airline.getCanDelete();
        canUpdate = !flightRepository.findAll().stream()
                .anyMatch(f -> f.getPlane().getAirline().getId().equals(airline.getId()));
        canDelete = !planeRepository.existsByAirline(airline);
        AirlineResponse response = AirlineResponse.builder()
                .id(airline.getId())
                .airlineCode(airline.getAirlineCode())
                .airlineName(airline.getAirlineName())
                .canDelete(canDelete)
                .canUpdate(canUpdate)
                .build();
        return response;
    }

    public void updateAirline(Airline airline, AirlineRequest request) {
        airline.setAirlineCode(request.getAirlineCode());
        airline.setAirlineName(request.getAirlineName());

    }
}

package com.bookingflight.app.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.dto.request.Flight_AirportRequest;
import com.bookingflight.app.dto.response.Flight_AirportResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.AirportRepository;
import com.bookingflight.app.repository.FlightRepository;

@Mapper(componentModel = "spring")
public interface Flight_AirportMapper {

        @Mapping(target = "flight", ignore = true)
        @Mapping(target = "airport", ignore = true)
        @Mapping(target = "id", ignore = true)
        Flight_Airport toFlight_Airport(Flight_AirportRequest request,
                        @Context FlightRepository flightRepository,
                        @Context AirportRepository airportRepository);

        @AfterMapping
        default void setAttributes(Flight_AirportRequest request, @MappingTarget Flight_Airport flight_Airport,
                        @Context FlightRepository flightRepository,
                        @Context AirportRepository airportRepository) {

                flight_Airport.setFlight(flightRepository.findById(request.getFlightId())
                                .orElseThrow(() -> new AppException(ErrorCode.FLIGHT_NOT_EXISTED)));
                flight_Airport.setAirport(airportRepository.findById(request.getAirportId())
                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)));
        }

        // Mapping từ Entity sang Response
        @Mapping(target = "flightId", ignore = true)
        @Mapping(target = "airportId", ignore = true)
        Flight_AirportResponse toFlight_AirportResponse(Flight_Airport entity,
                        @Context FlightRepository flightRepository,
                        @Context AirportRepository airportRepository);

        @AfterMapping
        default void setResponseAttributes(Flight_Airport entity, @MappingTarget Flight_AirportResponse response) {
                response.setFlightId(entity.getFlight().getId());
                response.setAirportId(entity.getAirport().getId());
        }
}

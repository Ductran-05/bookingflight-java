package com.bookingflight.app.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.dto.request.FlightRequest;
import com.bookingflight.app.dto.response.FlightResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.AirportRepository;
import com.bookingflight.app.repository.PlaneRepository;

@Mapper(componentModel = "spring")
public interface FlightMapper {

        @Mapping(target = "plane", ignore = true)
        @Mapping(target = "departureAirport", ignore = true)
        @Mapping(target = "arrivalAirport", ignore = true)
        Flight toFlight(FlightRequest request,
                        @Context PlaneRepository planeRepository,
                        @Context AirportRepository airportRepository);

        @AfterMapping
        default void setAttributes(FlightRequest request, @MappingTarget Flight flight,
                        @Context PlaneRepository planeRepository,
                        @Context AirportRepository airportRepository) {
                flight.setPlane(planeRepository.findById(request.getPlaneId())
                                .orElseThrow(() -> new AppException(ErrorCode.PLANE_NOT_EXISTED)));
                flight.setDepartureAirport(airportRepository.findById(request.getDepartureAirportId())
                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)));
                flight.setArrivalAirport(airportRepository.findById(request.getArrivalAirportId())
                                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)));
        }

        @Mapping(target = "planeId", source = "plane.id")
        @Mapping(target = "planeName", source = "plane.planeName")
        @Mapping(target = "departureAirportId", source = "departureAirport.id")
        @Mapping(target = "arrivalAirportId", source = "arrivalAirport.id")
        FlightResponse toFlightResponse(Flight flight);

}

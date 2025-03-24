package com.bookingflight.app.mapper;

import org.mapstruct.Mapper;

import com.bookingflight.app.domain.Flight_Airport;
import com.bookingflight.app.dto.request.Flight_AirportRequest;
import com.bookingflight.app.dto.response.Flight_AirportResponse;

@Mapper(componentModel = "spring")
public interface Flight_AirportMapper {
    Flight_Airport toFlight_Airport(Flight_AirportRequest request);

    Flight_AirportResponse toFlight_AirportResponse(Flight_Airport entity);
}

package com.bookingflight.app.mapper;

import org.mapstruct.Mapper;

import com.bookingflight.app.domain.Flight;
import com.bookingflight.app.dto.request.FlightRequest;
import com.bookingflight.app.dto.response.FlightResponse;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    Flight toFlight(FlightRequest request);

    FlightResponse toFlightResponse(Flight entity);
}

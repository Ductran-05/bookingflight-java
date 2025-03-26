package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Airline;
import com.bookingflight.app.dto.request.AirlineRequest;
import com.bookingflight.app.dto.response.AirlineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AirlineMapper {
    Airline toAirline (AirlineRequest request);
    AirlineResponse toAirlineResponse (Airline airline);

    void updateAirline(@MappingTarget Airline target, AirlineRequest request);
}

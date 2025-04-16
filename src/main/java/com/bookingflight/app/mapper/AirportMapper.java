package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.dto.request.AirportRequest;
import com.bookingflight.app.dto.response.AirportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AirportMapper {

    @Mapping(target = "cityId", source = "city.id")
    AirportResponse toAirportResponse(Airport airport);

    @Mapping(target = "city", ignore = true)
    Airport toAirport(AirportRequest airportRequest);

    @Mapping(target = "city", ignore = true)
    void updateAirport(@MappingTarget Airport airport, AirportRequest airportRequest);
}
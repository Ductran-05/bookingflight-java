package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.dto.request.AirportRequest;
import com.bookingflight.app.dto.response.AirportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CityMapper.class)
public interface AirportMapper {

    @Mapping(target = "cityCode", source = "city.cityCode")
    @Mapping(target = "cityName", source = "city.cityName")
    AirportResponse toAirportResponse(Airport airport);

    Airport toAirport(AirportRequest airportRequest);

    void updateAirport(@MappingTarget Airport airport, AirportRequest airportRequest);
}
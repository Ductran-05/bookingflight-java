package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.City;
import com.bookingflight.app.dto.request.CityRequest;
import com.bookingflight.app.dto.response.CityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CityMapper {
    City toCity(CityRequest cityRequest);
    CityResponse toCityResponse(City city);

    void updateCity(@MappingTarget City city, CityRequest request);
}

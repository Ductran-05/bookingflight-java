package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.domain.City;
import com.bookingflight.app.dto.request.AirportRequest;
import com.bookingflight.app.dto.response.AirportResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.CityRepository;
import com.bookingflight.app.repository.Flight_AirportRepository;
import lombok.RequiredArgsConstructor;

import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirportMapper {

    private final Flight_AirportRepository flight_AirportRepository;

    private final CityRepository cityRepository;

    public Airport toAirport(AirportRequest request) {
        Airport airport = new Airport();
        airport.setAirportCode(request.getAirportCode());
        airport.setAirportName(request.getAirportName());

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new AppException(ErrorCode.CITY_NOT_EXISTED));
        airport.setCity(city);

        return airport;
    }

    public void updateAirport(@MappingTarget Airport airport, AirportRequest request) {
        airport.setAirportCode(request.getAirportCode());
        airport.setAirportName(request.getAirportName());

        if (request.getCityId() != null) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new AppException(ErrorCode.CITY_NOT_EXISTED));
            airport.setCity(city);
        }
    }

    public AirportResponse toAirportResponse(Airport airport) {
        AirportResponse response = new AirportResponse();
        response.setId(airport.getId());
        response.setAirportCode(airport.getAirportCode());
        response.setAirportName(airport.getAirportName());

        if (airport.getCity() != null) {
            response.setCityId(airport.getCity().getId());
            response.setCityName(airport.getCity().getCityName());
        }
        response.setCanUpdate(!flight_AirportRepository.existsByAirport(airport));
        response.setCanDelete(!flight_AirportRepository.existsByAirport(airport));
        return response;
    }
}

package com.bookingflight.app.mapper;

import com.bookingflight.app.domain.City;
import com.bookingflight.app.dto.request.CityRequest;
import com.bookingflight.app.dto.response.CityResponse;
import com.bookingflight.app.repository.AirportRepository;
import com.bookingflight.app.repository.Flight_AirportRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class CityMapper {

    private final Flight_AirportRepository flight_AirportRepository;
    private final AirportRepository airportRepository;

    public City toCity(CityRequest request) {
        City city = City.builder()
                .cityCode(request.getCityCode())
                .cityName(request.getCityName())
                .build();
        return city;
    }

    public CityResponse toCityResponse(City city) {
        Boolean canUpdate = city.getCanUpdate();
        Boolean canDelete = city.getCanDelete();
        // kiểm tra nếu có bất kỳ chuyến bay nào có san bay thuộc thành phố
        // này thì trả về false
        canDelete = !flight_AirportRepository.findAll().stream()
                .anyMatch(flightAirport -> flightAirport.getAirport().getCity().getId().equals(city.getId()));
        canUpdate = !airportRepository.existsByCity(city);
        CityResponse cityResponse = CityResponse.builder()
                .id(city.getId())
                .cityCode(city.getCityCode())
                .cityName(city.getCityName())
                .canUpdate(canUpdate)
                .canDelete(canDelete)
                .build();
        return cityResponse;
    }

    public void updateCity(City city, CityRequest request) {
        city.setCityCode(request.getCityCode());
        city.setCityName(request.getCityName());
    }
}

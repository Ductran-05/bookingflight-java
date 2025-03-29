package com.bookingflight.app.service;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.domain.City;
import com.bookingflight.app.dto.request.AirportRequest;
import com.bookingflight.app.dto.response.AirportResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AirportMapper;
import com.bookingflight.app.repository.AirportRepository;
import com.bookingflight.app.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportService {
    private final AirportMapper airportMapper;
    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(airportMapper::toAirportResponse)
                .collect(Collectors.toList());
    }

    public AirportResponse getAirportById(String id) {
        return airportMapper.toAirportResponse(airportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)));
    }

    public AirportResponse createAirport(AirportRequest request) {

        City city = cityRepository.findByCityCode(request.getCityCode())
                .orElseThrow(() -> new AppException(ErrorCode.CITY_NOT_EXISTED));

        Airport airport = airportMapper.toAirport(request);
        airport.setCity(city); // City is now managed

        if (airportRepository.existsByAirportCode(airport.getAirportCode())) {
            throw new AppException(ErrorCode.AIRPORT_EXISTED);
        }

        return airportMapper.toAirportResponse(airportRepository.save(airport));
    }

    public AirportResponse updateAirport(String id, AirportRequest request) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED));
        airportMapper.updateAirport(airport, request);
        return airportMapper.toAirportResponse(airportRepository.save(airport));
    }

    public void deleteAirport(String id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED));
        airportRepository.delete(airport);
    }
}

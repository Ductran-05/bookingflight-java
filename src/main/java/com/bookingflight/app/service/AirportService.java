package com.bookingflight.app.service;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.domain.City;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.AirportRequest;
import com.bookingflight.app.dto.response.AirportResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AirportMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.repository.AirportRepository;
import com.bookingflight.app.repository.CityRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class AirportService {

    private final AirportMapper airportMapper;
    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;
    private final ResultPanigationMapper resultPanigationMapper;

    public ResultPaginationDTO getAllAirports(Specification<Airport> spec, Pageable pageable) {
        Page<AirportResponse> page = airportRepository.findAll(spec, pageable).map(airportMapper::toAirportResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public AirportResponse getAirportById(String id) {
        return airportMapper.toAirportResponse(airportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED)));
    }

    public AirportResponse createAirport(AirportRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new AppException(ErrorCode.CITY_NOT_EXISTED));

        Airport airport = airportMapper.toAirport(request);
        airport.setCity(city); // Set the city object

        if (airportRepository.existsByAirportCode(airport.getAirportCode())) {
            throw new AppException(ErrorCode.AIRPORT_EXISTED);
        }

        return airportMapper.toAirportResponse(airportRepository.save(airport));
    }

    public AirportResponse updateAirport(String id, AirportRequest request) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED));

        airportMapper.updateAirport(airport, request);

        if (request.getCityId() != null) {
            City city = cityRepository.findById(request.getCityId())
                    .orElseThrow(() -> new AppException(ErrorCode.CITY_NOT_EXISTED));
            airport.setCity(city);
        }

        return airportMapper.toAirportResponse(airportRepository.save(airport));
    }

    public void deleteAirport(String id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRPORT_NOT_EXISTED));
        airportRepository.delete(airport);
    }
}

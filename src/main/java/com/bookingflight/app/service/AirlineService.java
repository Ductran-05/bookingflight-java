package com.bookingflight.app.service;

import com.bookingflight.app.domain.Airline;
import com.bookingflight.app.dto.request.AirlineRequest;
import com.bookingflight.app.dto.response.AirlineResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AirlineMapper;
import com.bookingflight.app.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirlineService {
    private final AirlineMapper airlineMapper;
    private final AirlineRepository airlineRepository;

    public List<AirlineResponse> getAllAirlines() {
        return airlineRepository.findAll().stream()
                .map(airlineMapper::toAirlineResponse)
                .collect(Collectors.toList());
    }
    public AirlineResponse getAirlineById(String id) {
        return airlineMapper.toAirlineResponse(airlineRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED)));
    }
    public AirlineResponse createAirline(AirlineRequest request) {
        Airline airline = airlineMapper.toAirline(request);
        System.out.println("To Airline: " + airline);
        if(airlineRepository.existsByAirlineCode(airline.getAirlineCode())) {
            throw new AppException(ErrorCode.AIRLINE_EXISTED);
        }
        return airlineMapper.toAirlineResponse(airlineRepository.save(airline));
    }
    public AirlineResponse updateAirline(String id, AirlineRequest request) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED));
        airlineMapper.updateAirline(airline, request);
        return airlineMapper.toAirlineResponse(airlineRepository.save(airline));
    }
    public void deleteAirline(String id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED));
        airlineRepository.delete(airline);
    }
}

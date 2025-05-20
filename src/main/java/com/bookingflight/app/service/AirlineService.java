package com.bookingflight.app.service;

import com.bookingflight.app.domain.Airline;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.AirlineRequest;
import com.bookingflight.app.dto.response.AirlineResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.AirlineMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.repository.AirlineRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class AirlineService {

    private final ResultPanigationMapper resultPanigationMapper;
    private final AirlineMapper airlineMapper;
    private final AirlineRepository airlineRepository;

    public ResultPaginationDTO getAllAirline(Specification<Airline> spec, Pageable pageable) {
        Page<AirlineResponse> page = airlineRepository.findAll(spec, pageable).map(airlineMapper::toAirlineResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public AirlineResponse getAirlineById(String id) {
        return airlineMapper.toAirlineResponse(airlineRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED)));
    }

    public AirlineResponse createAirline(AirlineRequest request) {
        Airline airline = airlineMapper.toAirline(request);
        if (airlineRepository.existsByAirlineCode(airline.getAirlineCode())) {
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

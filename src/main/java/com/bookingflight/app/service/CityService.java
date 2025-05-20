package com.bookingflight.app.service;

import com.bookingflight.app.domain.City;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.CityRequest;
import com.bookingflight.app.dto.response.CityResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.CityMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.repository.CityRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityMapper cityMapper;
    private final CityRepository cityRepository;
    private final ResultPanigationMapper resultPanigationMapper;

    public ResultPaginationDTO getAllCities(Specification<City> spec, Pageable pageable) {
        Page<CityResponse> page = cityRepository.findAll(spec, pageable).map(cityMapper::toCityResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public CityResponse getCityById(String id) {
        return cityMapper.toCityResponse(cityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CITY_NOT_EXISTED)));
    }

    public CityResponse createCity(CityRequest request) {
        City city = cityMapper.toCity(request);
        if (cityRepository.existsByCityCode(city.getCityCode())) {
            throw new AppException(ErrorCode.CITY_EXISTED);
        }
        return cityMapper.toCityResponse(cityRepository.save(city));
    }

    public CityResponse updateCity(String id, CityRequest request) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CITY_NOT_EXISTED));
        cityMapper.updateCity(city, request);
        return cityMapper.toCityResponse(cityRepository.save(city));
    }

    public void deleteCity(String id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CITY_NOT_EXISTED));
        cityRepository.delete(city);
    }
}
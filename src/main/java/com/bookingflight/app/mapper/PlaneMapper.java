package com.bookingflight.app.mapper;

import org.springframework.stereotype.Component;

import com.bookingflight.app.domain.Airline;
import com.bookingflight.app.domain.Plane;
import com.bookingflight.app.dto.request.PlaneRequest;
import com.bookingflight.app.dto.response.PlaneResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.repository.AirlineRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaneMapper {

    private final AirlineRepository airlineRepository;

    public Plane toPlane(PlaneRequest request) {
        Plane plane = new Plane();
        plane.setPlaneCode(request.getPlaneCode());
        plane.setPlaneName(request.getPlaneName());

        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED));
        plane.setAirline(airline);

        return plane;
    }

    public void updatePlane(Plane plane, PlaneRequest request) {
        plane.setPlaneCode(request.getPlaneCode());
        plane.setPlaneName(request.getPlaneName());

        if (request.getAirlineId() != null) {
            Airline airline = airlineRepository.findById(request.getAirlineId())
                    .orElseThrow(() -> new AppException(ErrorCode.AIRLINE_NOT_EXISTED));
            plane.setAirline(airline);
        }
    }

    public PlaneResponse toPlaneResponse(Plane plane) {
        PlaneResponse response = new PlaneResponse();
        response.setId(plane.getId());
        response.setPlaneCode(plane.getPlaneCode());
        response.setPlaneName(plane.getPlaneName());

        if (plane.getAirline() != null) {
            response.setAirlineId(plane.getAirline().getId());
            response.setAirlineName(plane.getAirline().getAirlineName());
        }

        return response;
    }
}

package com.bookingflight.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.bookingflight.app.domain.Plane;
import com.bookingflight.app.dto.request.PlaneRequest;
import com.bookingflight.app.dto.response.PlaneResponse;

@Mapper(componentModel = "spring")
public interface PlaneMapper {
    Plane toPlane(PlaneRequest request);

    PlaneResponse toPlaneResponse(Plane entity);

    void updatePlane(@MappingTarget Plane plane, PlaneRequest request);

}

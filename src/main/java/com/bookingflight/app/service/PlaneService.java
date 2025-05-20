package com.bookingflight.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Plane;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.PlaneRequest;
import com.bookingflight.app.dto.response.PlaneResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.PlaneMapper;
import com.bookingflight.app.mapper.ResultPanigationMapper;
import com.bookingflight.app.repository.PlaneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaneService {
    private final PlaneRepository planeRepository;
    private final PlaneMapper planeMapper;
    private final ResultPanigationMapper resultPanigationMapper;

    public ResultPaginationDTO getAllPlanes(Specification<Plane> spec, Pageable pageable) {
        Page<PlaneResponse> page = planeRepository.findAll(spec, pageable)
                .map(planeMapper::toPlaneResponse);
        return resultPanigationMapper.toResultPanigationMapper(page);
    }

    public PlaneResponse getPlaneById(String id) {
        return planeMapper.toPlaneResponse(planeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLANE_NOT_EXISTED)));
    }

    public PlaneResponse createPlane(PlaneRequest request) {
        if (planeRepository.findByPlaneCode(request.getPlaneCode()) != null)
            throw new AppException(ErrorCode.PLANE_EXISTED);
        return planeMapper.toPlaneResponse(planeRepository.save(planeMapper.toPlane(request)));
    }

    public PlaneResponse updatePlane(String id, PlaneRequest request) {
        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLANE_NOT_EXISTED));
        planeMapper.updatePlane(plane, request);

        return planeMapper.toPlaneResponse(planeRepository.save(plane));
    }

    public void deletePlane(String id) {
        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PLANE_NOT_EXISTED));
        planeRepository.delete(plane);
    }
}

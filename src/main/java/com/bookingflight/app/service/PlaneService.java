package com.bookingflight.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bookingflight.app.domain.Plane;
import com.bookingflight.app.dto.request.PlaneRequest;
import com.bookingflight.app.dto.response.PlaneResponse;
import com.bookingflight.app.exception.AppException;
import com.bookingflight.app.exception.ErrorCode;
import com.bookingflight.app.mapper.PlaneMapper;
import com.bookingflight.app.repository.PlaneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaneService {
    private final PlaneRepository planeRepository;
    private final PlaneMapper planeMapper;

    public List<PlaneResponse> getAllPlanes(Specification<Plane> spec, Pageable pageable) {
        List<Plane> planes = planeRepository.findAll(spec, pageable).getContent();
        return planes.stream().map(planeMapper::toPlaneResponse).collect(Collectors.toList());
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

package com.bookingflight.app.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookingflight.app.domain.Plane;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.PlaneRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.PlaneResponse;
import com.bookingflight.app.service.PlaneService;
import com.turkraft.springfilter.boot.Filter;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlaneController {
    private final PlaneService planeService;

    @GetMapping()
    public ResponseEntity<APIResponse<ResultPaginationDTO>> getAllPlanes(@Filter Specification<Plane> spec,
            Pageable pageable) {
        APIResponse<ResultPaginationDTO> apiResponse = APIResponse.<ResultPaginationDTO>builder()
                .Code(200)
                .Message("Get all planes successfully")
                .data(planeService.getAllPlanes(spec, pageable))
                .build();
        return ResponseEntity.ok(apiResponse); // return HTTP status 200 with APIResponse object. //
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<PlaneResponse>> getPlaneById(@PathVariable("id") String id) {
        APIResponse<PlaneResponse> apiResponse = APIResponse.<PlaneResponse>builder()
                .Code(200)
                .Message("Get plane by id successfully")
                .data(planeService.getPlaneById(id))
                .build();
        return ResponseEntity.ok(apiResponse); // return HTTP status 200 with APIResponse object. //
    }

    @PostMapping()
    public ResponseEntity<APIResponse<PlaneResponse>> createPlane(@RequestBody @Valid PlaneRequest request) {
        APIResponse<PlaneResponse> apiResponse = APIResponse.<PlaneResponse>builder()
                .Code(201)
                .Message("Create plane successfully")
                .data(planeService.createPlane(request))
                .build();
        return ResponseEntity.ok(apiResponse); // return HTTP status 201 with APIResponse object. //
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<PlaneResponse>> updatePlane(@PathVariable("id") String id,
            @RequestBody PlaneRequest request) {
        APIResponse<PlaneResponse> apiResponse = APIResponse.<PlaneResponse>builder()
                .Code(200)
                .Message("Update plane successfully")
                .data(planeService.updatePlane(id, request))
                .build();
        return ResponseEntity.ok(apiResponse); // return HTTP status 200 with APIResponse object. //
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deletePlane(@PathVariable("id") String id) {
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .Code(204)
                .Message("Delete plane successfully")
                .build();
        planeService.deletePlane(id);
        return ResponseEntity.ok(apiResponse); // return HTTP status 204 with no content. //
    }
}
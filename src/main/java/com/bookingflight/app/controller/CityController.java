package com.bookingflight.app.controller;

import com.bookingflight.app.domain.City;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.CityRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.CityResponse;
import com.bookingflight.app.service.CityService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping
    public ResponseEntity<APIResponse<ResultPaginationDTO>> getCities(@Filter Specification<City> spec,
            Pageable pageable) {
        APIResponse<ResultPaginationDTO> response = APIResponse.<ResultPaginationDTO>builder()
                .Code(200)
                .Message("Success")
                .data(cityService.getAllCities(spec, pageable))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CityResponse>> getCityById(@PathVariable("id") String id) {
        APIResponse<CityResponse> response = APIResponse.<CityResponse>builder()
                .Code(200)
                .Message("Success")
                .data(cityService.getCityById(id))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<APIResponse<CityResponse>> createCity(@RequestBody @Valid CityRequest cityRequest) {
        APIResponse<CityResponse> response = APIResponse.<CityResponse>builder()
                .Code(200)
                .Message("Created city successfully")
                .data(cityService.createCity(cityRequest))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CityResponse>> updateCity(@PathVariable("id") String id,
            @RequestBody @Valid CityRequest cityRequest) {
        APIResponse<CityResponse> response = APIResponse.<CityResponse>builder()
                .Code(200)
                .Message("Success")
                .data(cityService.updateCity(id, cityRequest))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<CityResponse>> deleteCity(@PathVariable("id") String id) {
        cityService.deleteCity(id);
        APIResponse<CityResponse> response = APIResponse.<CityResponse>builder()
                .Code(200)
                .Message("Success")
                .build();
        return ResponseEntity.ok().body(response);
    }
}

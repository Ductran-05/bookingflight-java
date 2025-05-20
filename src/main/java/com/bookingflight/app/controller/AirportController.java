package com.bookingflight.app.controller;

import com.bookingflight.app.domain.Airport;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.AirportRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AirportResponse;
import com.bookingflight.app.service.AirportService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
public class AirportController {
        private final AirportService airportService;

        @GetMapping
        public ResponseEntity<APIResponse<ResultPaginationDTO>> getAirports(@Filter Specification<Airport> spec,
                        Pageable pageable) {
                APIResponse<ResultPaginationDTO> response = APIResponse.<ResultPaginationDTO>builder()
                                .Code(200)
                                .Message("Success")
                                .data(airportService.getAllAirports(spec, pageable))
                                .build();
                return ResponseEntity.ok().body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<APIResponse<AirportResponse>> getAirportById(@PathVariable("id") String id) {
                APIResponse<AirportResponse> response = APIResponse.<AirportResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(airportService.getAirportById(id))
                                .build();
                return ResponseEntity.ok().body(response);
        }

        @PostMapping
        public ResponseEntity<APIResponse<AirportResponse>> createAirport(
                        @RequestBody @Valid AirportRequest airportRequest) {
                APIResponse<AirportResponse> response = APIResponse.<AirportResponse>builder()
                                .Code(200)
                                .Message("Created airport successfully")
                                .data(airportService.createAirport(airportRequest))
                                .build();
                return ResponseEntity.ok().body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<APIResponse<AirportResponse>> updateAirport(@PathVariable("id") String id,
                        @RequestBody AirportRequest airportRequest) {

                APIResponse<AirportResponse> response = APIResponse.<AirportResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(airportService.updateAirport(id, airportRequest))
                                .build();
                return ResponseEntity.ok().body(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<APIResponse<AirportResponse>> deleteAirport(@PathVariable("id") String id) {
                airportService.deleteAirport(id);
                APIResponse<AirportResponse> response = APIResponse.<AirportResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .build();
                return ResponseEntity.ok().body(response);
        }
}
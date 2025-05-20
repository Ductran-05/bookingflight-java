package com.bookingflight.app.controller;

import com.bookingflight.app.domain.Airline;
import com.bookingflight.app.dto.ResultPaginationDTO;
import com.bookingflight.app.dto.request.AirlineRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AirlineResponse;
import com.bookingflight.app.service.AirlineService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airlines")
@RequiredArgsConstructor
public class AirlineController {
        private final AirlineService airlineService;

        @GetMapping
        public ResponseEntity<APIResponse<ResultPaginationDTO>> getAirlines(@Filter Specification<Airline> spec,
                        Pageable pageable) {
                APIResponse<ResultPaginationDTO> response = APIResponse.<ResultPaginationDTO>builder()
                                .Code(200)
                                .Message("Success")
                                .data(airlineService.getAllAirline(spec, pageable))
                                .build();
                return ResponseEntity.ok().body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<APIResponse<AirlineResponse>> getAirlineById(@PathVariable("id") String id) {
                APIResponse<AirlineResponse> response = APIResponse.<AirlineResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(airlineService.getAirlineById(id))
                                .build();
                return ResponseEntity.ok().body(response);
        }

        @PostMapping
        public ResponseEntity<APIResponse<AirlineResponse>> createAirline(
                        @RequestBody @Valid AirlineRequest airlineRequest) {
                System.out.println("Request: " + airlineRequest);
                APIResponse<AirlineResponse> response = APIResponse.<AirlineResponse>builder()
                                .Code(200)
                                .Message("created airline successfully")
                                .data(airlineService.createAirline(airlineRequest))
                                .build();
                System.out.println("Response: " + response);
                return ResponseEntity.ok().body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<APIResponse<AirlineResponse>> updateAirline(@PathVariable("id") String id,
                        @RequestBody AirlineRequest airlineRequest) {
                APIResponse<AirlineResponse> response = APIResponse.<AirlineResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .data(airlineService.updateAirline(id, airlineRequest))
                                .build();
                return ResponseEntity.ok().body(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<APIResponse<AirlineResponse>> deleteAirline(@PathVariable("id") String id) {
                airlineService.deleteAirline(id);
                APIResponse<AirlineResponse> response = APIResponse.<AirlineResponse>builder()
                                .Code(200)
                                .Message("Success")
                                .build();
                return ResponseEntity.ok().body(response);
        }
}

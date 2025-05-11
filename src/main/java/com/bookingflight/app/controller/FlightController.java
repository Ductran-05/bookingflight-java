package com.bookingflight.app.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookingflight.app.domain.Seat;
import com.bookingflight.app.dto.request.FlightRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.FlightResponse;
import com.bookingflight.app.dto.response.SeatResponse;
import com.bookingflight.app.service.FlightService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @GetMapping()
    public ResponseEntity<APIResponse<List<FlightResponse>>> getAllFlights() {
        APIResponse<List<FlightResponse>> response = APIResponse.<List<FlightResponse>>builder()
                .Code(200)
                .Message("Success")
                .data(flightService.getAllFlights())
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<FlightResponse>> getFlightById(@PathVariable("id") String id) {
        APIResponse<FlightResponse> response = APIResponse.<FlightResponse>builder()
                .Code(200)
                .Message("Success")
                .data(flightService.getFlightById(id))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping()
    public ResponseEntity<APIResponse<FlightResponse>> createFlight(@RequestBody @Valid FlightRequest request) {
        APIResponse<FlightResponse> response = APIResponse.<FlightResponse>builder()
                .Code(200)
                .Message("Success")
                .data(flightService.createFlight(request))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<FlightResponse>> updateFlight(@PathVariable("id") String id,
            @RequestBody FlightRequest request) {
        APIResponse<FlightResponse> response = APIResponse.<FlightResponse>builder()
                .Code(200)
                .Message("Success")
                .data(flightService.updateFlight(id, request))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<FlightResponse>> deleteFlight(@PathVariable("id") String id) {
        flightService.deleteFlight(id);
        APIResponse<FlightResponse> response = APIResponse.<FlightResponse>builder()
                .Code(200)
                .Message("Success")
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/seats/{id}")
    public ResponseEntity<APIResponse<List<SeatResponse>>> getSeatsByFlightId(@PathVariable("id") String id) {
        APIResponse<List<SeatResponse>> response = APIResponse.<List<SeatResponse>>builder()
                .Code(200)
                .Message("Success")
                .data(flightService.getSeatsByFlightId(id))
                .build();
        return ResponseEntity.ok().body(response);
    }

}

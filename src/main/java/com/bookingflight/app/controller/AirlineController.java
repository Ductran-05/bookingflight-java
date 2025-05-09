package com.bookingflight.app.controller;

import com.bookingflight.app.dto.request.AirlineRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.AirlineResponse;
import com.bookingflight.app.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airlines")
@RequiredArgsConstructor
public class AirlineController {
    private final AirlineService airlineService;

    @GetMapping
    public ResponseEntity<APIResponse<List<AirlineResponse>>> getAirlines() {
        APIResponse<List<AirlineResponse>> response = APIResponse.<List<AirlineResponse>>builder()
                .Code(200)
                .Message("Success")
                .data(airlineService.getAllAirlines())
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
    public ResponseEntity<APIResponse<AirlineResponse>> createAirline(@RequestBody AirlineRequest airlineRequest) {
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

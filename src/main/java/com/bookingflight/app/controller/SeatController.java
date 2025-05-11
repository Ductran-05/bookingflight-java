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

import com.bookingflight.app.dto.request.SeatRequest;
import com.bookingflight.app.dto.response.APIResponse;
import com.bookingflight.app.dto.response.SeatResponse;
import com.bookingflight.app.service.SeatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping()
    public ResponseEntity<APIResponse<List<SeatResponse>>> getAllSeats() {
        APIResponse<List<SeatResponse>> apiResponse = APIResponse.<List<SeatResponse>>builder()
                .Code(200)
                .Message("Get all seats successfully")
                .data(seatService.getAllSeats())
                .build();
        return ResponseEntity.ok(apiResponse); // return HTTP status 200 with APIResponse object. //

    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<SeatResponse>> getSeatById(@PathVariable("id") String id) {
        APIResponse<SeatResponse> apiResponse = APIResponse.<SeatResponse>builder()
                .Code(200)
                .Message("Get seat by id successfully")
                .data(seatService.getSeatById(id))
                .build();
        return ResponseEntity.ok(apiResponse); // return HTTP status 200 with APIResponse object.
    }

    @PostMapping()
    public ResponseEntity<APIResponse<SeatResponse>> createSeat(@RequestBody @Valid SeatRequest request) {
        APIResponse<SeatResponse> apiResponse = APIResponse.<SeatResponse>builder()
                .Code(201)
                .Message("Create seat successfully")
                .data(seatService.createSeat(request))
                .build();
        return ResponseEntity.ok(apiResponse); // return HTTP status 201 with APIResponse object.
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<SeatResponse>> updateSeat(@PathVariable("id") String id,
            @RequestBody SeatRequest request) {
        APIResponse<SeatResponse> apiResponse = APIResponse.<SeatResponse>builder()
                .Code(200)
                .Message("Update seat successfully")
                .data(seatService.updateSeat(id, request))
                .build();
        return ResponseEntity.ok(apiResponse); // return HTTP status 200 with APIResponse object.
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteSeat(@PathVariable("id") String id) {
        APIResponse<Void> apiResponse = APIResponse.<Void>builder()
                .Code(204)
                .Message("Delete seat successfully")
                .build();
        seatService.deleteSeat(id);
        return ResponseEntity.ok(apiResponse); // return HTTP status 204 with no content.
    }

}

package com.bookingflight.app.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightRequest {

    @NotBlank(message = "Flight code is required")
    @Size(max = 10, message = "Flight code must be at most 10 characters")
    String flightCode;

    @NotBlank(message = "Plane ID is required")
    String planeId;

    @NotBlank(message = "Departure airport ID is required")
    String departureAirportId;

    @NotBlank(message = "Arrival airport ID is required")
    String arrivalAirportId;

    @NotNull(message = "Departure time is required")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime arrivalTime;

    @NotNull(message = "Origin price is required")
    @Min(value = 0, message = "Origin price must be zero or positive")
    Number originPrice;

    @Valid
    @NotEmpty(message = "Flight airport list cannot be empty")
    List<Flight_AirportRequest> listFlight_Airport;

    @Valid
    @NotEmpty(message = "Flight seat list cannot be empty")
    List<Flight_SeatRequest> listFlight_Seat;
}

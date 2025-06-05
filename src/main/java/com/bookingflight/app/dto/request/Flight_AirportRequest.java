package com.bookingflight.app.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight_AirportRequest {

    // Nếu cần bắt buộc có flightId thì mở @NotBlank ra
    // @NotBlank(message = "Flight ID is required")
    String flightId;

    @NotBlank(message = "Airport ID is required")
    String airportId;

    @NotNull(message = "Departure time is required")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime arrivalTime;

    @Size(max = 200, message = "Note must not exceed 200 characters")
    String note;
}

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
    @NotBlank(message = "FLIGHT_ID_IS_REQUIRED")
    String flightId;

    @NotBlank(message = "AIRPORT_ID_IS_REQUIRED")
    String airportId;

    @NotNull(message = "DEPARTURE_TIME_IS_REQUIRED")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime departureTime;

    @NotNull(message = "ARRIVAL_TIME_IS_REQUIRED")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime arrivalTime;

    @Size(max = 200, message = "NOTE_TOO_LONG")
    String note;

}

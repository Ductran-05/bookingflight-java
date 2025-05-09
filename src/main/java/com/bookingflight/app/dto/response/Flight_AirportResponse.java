package com.bookingflight.app.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight_AirportResponse {
    String id;
    String flightId;
    String airportId;
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime departureTime;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime arrivalTime;

    String note;
}

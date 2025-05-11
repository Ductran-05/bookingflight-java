package com.bookingflight.app.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
public class FlightRequest {
    @NotBlank(message = "FLIGHT_CODE_IS_REQUIRED")
    @Size(max = 10, message = "FLIGHT_CODE_INVALID")
    String flightCode;

    @NotBlank(message = "PLANE_ID_IS_REQUIRED")
    String planeId;

    @NotBlank(message = "DEPARTURE_AIRPORT_ID_IS_REQUIRED")
    String departureAirportId;

    @NotBlank(message = "ARRIVAL_AIRPORT_ID_IS_REQUIRED")
    String arrivalAirportId;

    @NotNull(message = "DEPARTURE_TIME_IS_REQUIRED")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime departureTime;

    @NotNull(message = "ARRIVAL_TIME_IS_REQUIRED")
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime arrivalTime;

    @NotNull(message = "ORIGIN_PRICE_IS_REQUIRED")
    @Min(value = 0, message = "ORIGIN_PRICE_MUST_BE_POSITIVE")
    Double originPrice;

    @Valid
    List<@NotNull Flight_AirportRequest> listFlight_Airport;

    @Valid
    List<@NotNull Flight_SeatRequest> listFlight_Seat;
}

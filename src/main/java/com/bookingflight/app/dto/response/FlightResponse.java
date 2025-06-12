package com.bookingflight.app.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.bookingflight.app.domain.FlightStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightResponse {
    String id;
    String flightCode;
    String planeId;
    String planeName;
    String departureAirportId;
    String departureAirportName;
    String arrivalAirportId;
    String arrivalAirportName;
    FlightStatus flightStatus;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime departureTime;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime arrivalTime;

    BigDecimal originPrice;

    List<Flight_AirportResponse> listFlight_Airport;
    List<Flight_SeatResponse> listFlight_Seat;
}

package com.bookingflight.app.dto.response;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightResponse {
    String id;
    String flightCode;
    String flightName;
    String planeId;
    String planeName;
    String departureAirportId;
    String departureAirportName;
    String arrivalAirportId;
    String arrivalAirportName;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime departureTime;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime arrivalTime;

    Number originPrice;

    List<Flight_AirportResponse> intermediateAirports;
    List<Flight_SeatResponse> listFlight_SeatResponses;
}

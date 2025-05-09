package com.bookingflight.app.dto.response;

import java.sql.Date;
import java.util.List;

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
    String planeId;
    String planeName;
    String departureAirportId;
    String arrivalAirportId;
    Date departureTime;
    Date arrivalTime;
    Number originPrice;

    List<Flight_AirportResponse> intermediateAirports;
    List<Flight_SeatResponse> listFlight_SeatResponses;
}

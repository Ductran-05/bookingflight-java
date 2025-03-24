package com.bookingflight.app.dto.request;

import java.sql.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlightRequest {
    String flightCode;
    String planeId;
    String departureAirportId;
    String arrivalAirportId;
    Date departureTime;
    Date arrivalTime;
    Number originPrice;

    List<Flight_AirportRequest> intermediateAirports;
    List<Flight_SeatRequest> listFlight_Seat;
}

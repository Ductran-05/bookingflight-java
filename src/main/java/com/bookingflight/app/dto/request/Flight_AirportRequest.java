package com.bookingflight.app.dto.request;

import java.sql.Date;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight_AirportRequest {
    String flightId;
    String airportId;
    Date departureTime;
    Date arrivalTime;
    String note;

}

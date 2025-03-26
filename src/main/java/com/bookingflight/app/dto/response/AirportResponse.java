package com.bookingflight.app.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirportResponse {
    String id;
    String airportCode;
    String airportName;
    String cityCode;
    String cityName;
}

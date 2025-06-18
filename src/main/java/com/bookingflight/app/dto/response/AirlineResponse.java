package com.bookingflight.app.dto.response;

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
public class AirlineResponse {
    String id;
    String airlineCode;
    String airlineName;
    Boolean canUpdate;
    Boolean canDelete;
}

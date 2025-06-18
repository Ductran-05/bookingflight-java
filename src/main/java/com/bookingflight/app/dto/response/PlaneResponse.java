package com.bookingflight.app.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaneResponse {
    String id;
    String planeCode;
    String planeName;
    String airlineId;
    String airlineName;
    Boolean canUpdate;
    Boolean canDelete;
}

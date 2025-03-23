package com.bookingflight.app.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatResponse {
    String id;
    String seatCode;
    String seatName;
    Number price;
    String description;
}

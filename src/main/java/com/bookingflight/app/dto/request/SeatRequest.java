package com.bookingflight.app.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatRequest {
    String seatCode;
    String seatName;
    Number price;
    String description;
}

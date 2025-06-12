package com.bookingflight.app.dto.response;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight_SeatResponse {
    String id;
    String flightId;
    String seatId;
    String seatName;
    BigDecimal price;
    Integer quantityBooked;
    Integer quantityAvailable;
    Integer quantity;
}

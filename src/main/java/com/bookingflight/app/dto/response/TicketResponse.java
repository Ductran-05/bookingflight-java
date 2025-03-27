package com.bookingflight.app.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponse {
    String Id;
    String flightId;
    String seatId;
    String passengerName;
    String passengerPhone;
    String passengerIDCard;
    String passengerEmail;
}

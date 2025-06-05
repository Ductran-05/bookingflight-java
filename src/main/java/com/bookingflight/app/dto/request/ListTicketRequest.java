package com.bookingflight.app.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListTicketRequest {
    String flightId;
    String accountId;
    String seatId;
    List<Passenger> passengers;

    @Data
    @NoArgsConstructor
    public static class Passenger {
        String passengerName;
        String passengerPhone;
        String passengerIDCard;
        String passengerEmail;
        Boolean haveBaggage;
    }
}

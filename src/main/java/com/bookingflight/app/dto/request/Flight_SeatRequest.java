package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight_SeatRequest {
//    @NotBlank(message = "FLIGHT_ID_IS_REQUIRED")
    String flightId;

    @NotBlank(message = "SEAT_ID_IS_REQUIRED")
    String seatId;

    @NotNull(message = "QUANTITY_IS_REQUIRED")
    @Min(value = 1, message = "QUANTITY_MUST_BE_AT_LEAST_1")
    Number quantity;
}

package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatRequest {
    @NotBlank(message = "SEAT_CODE_IS_REQUIRED")
    @Size(min = 1, max = 10, message = "SEAT_CODE_INVALID")
    String seatCode;

    @NotBlank(message = "SEAT_NAME_IS_REQUIRED")
    @Size(max = 50, message = "SEAT_NAME_INVALID")
    String seatName;

    @NotNull(message = "PRICE_IS_REQUIRED")
    @Min(value = 0, message = "PRICE_MUST_BE_POSITIVE")
    Double price;

    @Size(max = 200, message = "DESCRIPTION_TOO_LONG")
    String description;
}

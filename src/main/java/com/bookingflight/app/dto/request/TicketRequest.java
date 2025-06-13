package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketRequest {
    @NotBlank(message = "FLIGHT_ID_IS_REQUIRED")
    String flightId;

    @NotBlank(message = "SEAT_ID_IS_REQUIRED")
    String seatId;

    @NotBlank(message = "ACCOUNT_ID_IS_REQUIRED")
    String accountId;

    @NotBlank(message = "PASSENGER_NAME_IS_REQUIRED")
    @Size(max = 100, message = "PASSENGER_NAME_INVALID")
    String passengerName;

    @NotBlank(message = "PASSENGER_PHONE_IS_REQUIRED")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9}$", message = "PASSENGER_PHONE_INVALID")
    String passengerPhone;

    @NotBlank(message = "PASSENGER_ID_CARD_IS_REQUIRED")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "PASSENGER_ID_CARD_INVALID")
    String passengerIDCard;

    @NotBlank(message = "PASSENGER_EMAIL_IS_REQUIRED")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "PASSENGER_EMAIL_INVALID")
    String passengerEmail;
    String urlImage;

    @NotNull(message = "BAGGAGE_STATUS_IS_REQUIRED")
    Boolean haveBaggage;
}

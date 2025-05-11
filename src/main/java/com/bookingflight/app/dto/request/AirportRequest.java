package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirportRequest {
    @NotBlank(message = "AIRPORT_CODE_IS_REQUIRED")
    @Pattern(regexp = "^[A-Z]{3}$", message = "AIRPORT_CODE_INVALID")
    String airportCode;

    @NotBlank(message = "AIRPORT_NAME_IS_REQUIRED")
    @Size(max = 50, message = "AIRPORT_NAME_INVALID")
    String airportName;

    @NotBlank(message = "CITY_ID_IS_REQUIRED")
    String cityId;
}

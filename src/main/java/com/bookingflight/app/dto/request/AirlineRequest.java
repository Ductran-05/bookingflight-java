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
public class AirlineRequest {

    @NotBlank(message = "AIRLINE_CODE_IS_REQUIRED")
    @Pattern(regexp = "^[A-Z]{2,3}$", message = "AIRLINE_CODE_INVALID")
    String airlineCode;

    @NotBlank(message = "AIRLINE_NAME_IS_REQUIRED")
    @Size(max = 30, message = "AIRLINE_NAME_INVALID")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,&'-]+$", message = "AIRLINE_NAME_INVALID")
    String airlineName;
}
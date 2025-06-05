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

    @NotBlank(message = "Airport code is required")
    @Pattern(regexp = "^[A-Z0-9]{2,10}$", message = "Airport code must be 2–10 uppercase letters or numbers")
    String airportCode;

    @NotBlank(message = "Airport name is required")
    @Size(max = 50, message = "Airport name must be at most 50 characters long")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,&'\\-]{2,50}$", message = "Airport name can only contain letters, numbers, spaces, and . , & ' -")
    String airportName;

    @NotBlank(message = "City ID is required")
    String cityId;
}

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

    @NotBlank(message = "Airline code is required")
    @Pattern(regexp = "^[A-Z0-9]{2,10}$", message = "Airline code must be between 2 and 10 characters and uppercase")
    String airlineCode;

    @NotBlank(message = "Airline name is required")
    @Size(max = 30, message = "Airline name must be at most 30 characters long")
    @Pattern(regexp = "^[A-Za-z0-9\\s.,&'\\-]{2,30}$", message = "Airline name can only contain letters, numbers, spaces, and characters . , & ' -")
    String airlineName;
}
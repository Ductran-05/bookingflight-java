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
public class CityRequest {

    @NotBlank(message = "City code is required")
    @Size(min = 2, max = 10, message = "City code must be between 2 and 10 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "City code must contain only uppercase letters and digits")
    String cityCode;

    @NotBlank(message = "City name is required")
    @Size(max = 100, message = "City name must not exceed 100 characters")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s'\\-]+$", message = "City name must contain only letters, spaces, apostrophes, and hyphens")
    String cityName;
}

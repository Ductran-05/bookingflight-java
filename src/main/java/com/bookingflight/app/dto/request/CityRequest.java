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

    @NotBlank(message = "CITY_IS_REQUIRED")
    @Size(min = 2, max = 10, message = "CODE_SIZE_INVALID")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "CODE_FORMAT_INVALID")
    String cityCode;

    @NotBlank(message = "NAME_IS_REQUIRED")
    @Size(max = 100, message = "NAME_SIZE_INVALID")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "NAME_FORMAT_INVALID")
    String cityName;
}

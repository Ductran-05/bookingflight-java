package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaneRequest {
    @NotBlank(message = "PLANE_CODE_IS_REQUIRED")
    @Size(min = 3, max = 10, message = "PLANE_CODE_INVALID")
    String planeCode;

    @NotBlank(message = "PLANE_NAME_IS_REQUIRED")
    @Size(max = 50, message = "PLANE_NAME_INVALID")
    String planeName;

    @NotBlank(message = "AIRLINE_ID_IS_REQUIRED")
    String airlineId;
}

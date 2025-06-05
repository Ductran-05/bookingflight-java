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
public class PlaneRequest {

    @NotBlank(message = "Plane code is required")
    @Size(min = 3, max = 10, message = "Plane code must be between 3 and 10 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Plane code must contain only uppercase letters and numbers")
    String planeCode;

    @NotBlank(message = "Plane name is required")
    @Size(max = 50, message = "Plane name must not exceed 50 characters")
    // Optional: bạn có thể thêm regex nếu muốn giới hạn ký tự hợp lệ trong tên
    @Pattern(regexp = "^[A-Za-z0-9\\s.,&'\\-]+$", message = "Plane name contains invalid characters")
    String planeName;

    @NotBlank(message = "Airline ID is required")
    String airlineId;
}

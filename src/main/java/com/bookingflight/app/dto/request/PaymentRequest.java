package com.bookingflight.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
    
    @NotNull(message = "AMOUNT_IS_REQUIRED")
    @Min(value = 1000, message = "AMOUNT_MUST_BE_AT_LEAST_1000")
    Integer amount;
    
    @NotBlank(message = "ORDER_INFO_IS_REQUIRED")
    String orderInfo;
    
    String bankCode;
    
    String language = "vn";
} 
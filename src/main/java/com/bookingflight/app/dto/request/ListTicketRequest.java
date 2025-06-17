package com.bookingflight.app.dto.request;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListTicketRequest {

    @NotBlank(message = "Flight ID is required")
    String flightId;

    String accountId;

    @NotBlank(message = "Seat ID is required")
    String seatId;

    @NotEmpty(message = "Passengers list cannot be empty")
    List<Passenger> passengers;

    @Data
    @NoArgsConstructor
    public static class Passenger {
        String ticketId;
        Long seatNumber;

        @NotBlank(message = "Passenger name is required")
        @Size(max = 50, message = "Passenger name must be at most 50 characters")
        String passengerName;

        @NotBlank(message = "Passenger phone is required")
        // Ví dụ số điện thoại chỉ gồm số, từ 7 đến 15 ký tự
        @Pattern(regexp = "^[0-9]{7,15}$", message = "Passenger phone must be numeric and 7 to 15 digits")
        String passengerPhone;

        @NotBlank(message = "Passenger ID card is required")
        @Size(max = 20, message = "Passenger ID card must be at most 20 characters")
        String passengerIDCard;

        @NotBlank(message = "Passenger email is required")
        // Email regex cơ bản
        @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$", message = "Passenger email is invalid")
        String passengerEmail;
        String urlImage;
        // Có thể null, nên không bắt buộc
        Boolean haveBaggage;
    }
}

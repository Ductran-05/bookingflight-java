package com.bookingflight.app.exception;

/**
 * 400 - Bad Request (client error, validation issues)
 * 401 - Unauthorized (authentication issues)
 * 403 - Forbidden (authorization issues)
 * 404 - Not Found (resource doesn't exist)
 * 409 - Conflict (resource already exists)
 * 422 - Unprocessable Entity (semantic errors)
 * 500 - Internal Server Error (server-side issues)
 */
public enum ErrorCode {
    // Conflict errors (resource already exists)
    USER_EXISTED(409, "User already exists"),
    AIRPORT_EXISTED(409, "Airport already exists"),
    EMAIL_EXISTED(409, "Email already exists"),
    FLIGHT_EXISTED(409, "Flight number already exists"),
    BOOKING_EXISTED(409, "Booking ID already exists"),
    ACCOUNT_USERNAME_EXISTED(409, "Account username already exists"),
    ACCOUNT_EMAIL_EXISTED(409, "Account email already exists"),
    ACCOUNT_PHONE_EXISTED(409, "Account phone already exists"),
    TICKET_EXISTED(409, "Ticket number already exists"),
    SEAT_EXISTED(409, "Seat already exists"),
    PLANE_EXISTED(409, "Plane already exists"),
    AIRLINE_EXISTED(409, "Airline already exists"),
    CITY_EXISTED(409, "City already exists"),
    PERMISSION_EXISTED(409, "Permission already exists"),
    ROLE_ALREADY_EXISTS(409, "Role already exists"),

    // Not Found errors (resource doesn't exist)
    USER_NOT_EXISTED(404, "User not found"),
    AIRPORT_NOT_EXISTED(404, "Airport not found"),
    FLIGHT_NOT_EXISTED(404, "Flight not found"),
    BOOKING_NOT_EXISTED(404, "Booking not found"),
    ACCOUNT_NOT_EXISTED(404, "Account not found"),
    PLANE_NOT_EXISTED(404, "Plane not found"),
    CUSTOMER_NOT_EXISTED(404, "Customer not found"),
    TICKET_NOT_EXISTED(404, "Ticket not found"),
    TICKET_NOT_FOUND(404, "Ticket not found"),
    SEAT_NOT_EXISTED(404, "Seat not found"),
    AIRLINE_NOT_EXISTED(404, "Airline not found"),
    CITY_NOT_EXISTED(404, "City not found"),
    FLIGHT_SEAT_NOT_EXISTED(404, "Flight seat not found"),
    FLIGHT_AIRPORT_NOT_EXISTED(404, "Flight airport not found"),
    FLIGHT_NOT_FOUND(404, "Flight not found"),
    PERMISSION_NOT_FOUND(404, "Permission not found"),
    ROLE_NOT_FOUND(404, "Role not found"),
    FILE_NOT_FOUND(404, "File not found"),

    // Authentication errors
    INCORRECT_PASSWORD(401, "Incorrect password"),

    // Validation errors (Bad Request)
    USERNAME_IS_REQUIRED(400, "Username is required"),
    USERNAME_INVALID(400, "Username must be between 4 and 20 characters"),
    PASSWORD_INVALID(400, "Password must be between 6 and 50 characters"),
    PASSWORD_IS_REQUIRED(400, "Password is required"),
    EMAIL_IS_REQUIRED(400, "Email must not be blank"),
    EMAIL_INVALID(400, "Invalid email format"),
    FULL_NAME_IS_REQUIRED(400, "Full name must not be blank"),
    FULL_NAME_INVALID(400, "Full name must be at most 50 characters"),
    PHONE_IS_REQUIRED(400, "Phone number must not be blank"),
    PHONE_INVALID(400, "Invalid phone number format"),
    ROLE_IS_REQUIRED(400, "Role must not be null"),
    ROLE_INVALID(400, "Role must be a positive number"),
    CITY_IS_REQUIRED(400, "City code is required"),
    AIRLINE_CODE_IS_REQUIRED(400, "Airline code is required"),
    AIRLINE_CODE_INVALID(400, "Airline code must be 2–3 characters"),
    AIRLINE_NAME_IS_REQUIRED(400, "Airline name is required"),
    AIRLINE_NAME_INVALID(400, "Airline name must be at most 30 characters"),
    PLANE_CODE_IS_REQUIRED(400, "Plane code is required"),
    PLANE_CODE_INVALID(400, "Plane code must be 3–10 characters"),
    PLANE_NAME_IS_REQUIRED(400, "Plane name is required"),
    PLANE_NAME_INVALID(400, "Plane name must be at most 50 characters"),
    AIRLINE_ID_IS_REQUIRED(400, "Airline ID is required"),
    SEAT_CODE_IS_REQUIRED(400, "Seat code is required"),
    SEAT_CODE_INVALID(400, "Seat code must be 1–10 characters"),
    SEAT_NAME_IS_REQUIRED(400, "Seat name is required"),
    SEAT_NAME_INVALID(400, "Seat name must be at most 50 characters"),
    PRICE_IS_REQUIRED(400, "Price is required"),
    PRICE_MUST_BE_POSITIVE(400, "Price must be zero or greater"),
    DESCRIPTION_TOO_LONG(400, "Description must be at most 200 characters"),
    FLIGHT_ID_IS_REQUIRED(400, "Flight ID is required"),
    SEAT_ID_IS_REQUIRED(400, "Seat ID is required"),
    PASSENGER_NAME_IS_REQUIRED(400, "Passenger name is required"),
    PASSENGER_NAME_INVALID(400, "Passenger name must be at most 100 characters"),
    PASSENGER_PHONE_IS_REQUIRED(400, "Passenger phone is required"),
    PASSENGER_PHONE_INVALID(400, "Passenger phone format is invalid"),
    PASSENGER_ID_CARD_IS_REQUIRED(400, "Passenger ID card is required"),
    PASSENGER_ID_CARD_INVALID(400, "Passenger ID card format is invalid"),
    PASSENGER_EMAIL_IS_REQUIRED(400, "Passenger email is required"),
    PASSENGER_EMAIL_INVALID(400, "Passenger email format is invalid"),
    BAGGAGE_STATUS_IS_REQUIRED(400, "Baggage status is required"),
    FLIGHT_CODE_IS_REQUIRED(400, "Flight code is required"),
    FLIGHT_CODE_INVALID(400, "Flight code format is invalid"),
    PLANE_ID_IS_REQUIRED(400, "Plane ID is required"),
    DEPARTURE_AIRPORT_ID_IS_REQUIRED(400, "Departure airport ID is required"),
    ARRIVAL_AIRPORT_ID_IS_REQUIRED(400, "Arrival airport ID is required"),
    DEPARTURE_TIME_INVALID(400, "Departure time is required and must match format"),
    ARRIVAL_TIME_INVALID(400, "Arrival time is required and must match format"),
    ORIGIN_PRICE_INVALID(400, "Origin price must be provided and >= 0"),
    CODE_SIZE_INVALID(400, "Code must be between 2 and 10 characters"),
    CODE_FORMAT_INVALID(400, "Code must be uppercase letters and/or digits"),
    NAME_IS_REQUIRED(400, "Name is required"),
    NAME_SIZE_INVALID(400, "Name must not exceed 100 characters"),
    NAME_FORMAT_INVALID(400, "Name must contain only letters and spaces"),
    CODE_IS_REQUIRED(400, "Code is required"),
    
    // File upload errors
    FILE_EMPTY(400, "File is empty"),
    FILE_TOO_LARGE(400, "File size exceeds maximum limit of 5MB"),
    INVALID_FILE_NAME(400, "Invalid file name"),
    INVALID_FILE_TYPE(400, "Invalid file type. Only JPG, JPEG, PNG, GIF are allowed"),
    FILE_UPLOAD_FAILED(500, "File upload failed"),
    AVATAR_URL_TOO_LONG(400, "Avatar URL too long"),
    
    // Server errors
    UNIDENTIFIED_EXCEPTION(500, "Unidentified server error"),;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
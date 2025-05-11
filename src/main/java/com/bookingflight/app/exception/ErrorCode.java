package com.bookingflight.app.exception;

import com.bookingflight.app.domain.City;

public enum ErrorCode {
    USER_EXISTED(1001, "User existed"),
    USER_NOT_EXISTED(1003, "User not existed"),
    UNIDENTIFIED_EXCEPTION(9999, "Unidentified exception"),
    INCORRECT_PASSWORD(1004, "Incorrect password"),
    AIRPORT_EXISTED(1001, "Airport existed"),
    AIRPORT_NOT_EXISTED(1003, "Airport not existed"),
    EMAIL_EXISTED(1001, "Email existed"),
    FLIGHT_EXISTED(1001, "Flight's number existed"),
    FLIGHT_NOT_EXISTED(1003, "Flight not existed"),
    BOOKING_EXISTED(1001, "Booking's id existed"),
    BOOKING_NOT_EXISTED(1003, "Booking not existed"),
    ACCOUNT_NOT_EXISTED(1003, "Account not existed"),
    ACCOUNT_USERNAME_EXISTED(1003, "Account username already existed"),
    ACCOUNT_EMAIL_EXISTED(1003, "Account email already existed"),
    ACCOUNT_PHONE_EXISTED(1003, "Account phone already existed"),
    PLANE_NOT_EXISTED(1003, "Plane not existed"),
    CUSTOMER_NOT_EXISTED(1003, "Customer not existed"),
    TICKET_EXISTED(1001, "Ticket number existed"),
    TICKET_NOT_EXISTED(1003, "Ticket number not existed"),
    SEAT_EXISTED(1001, "Seat existed"),
    SEAT_NOT_EXISTED(1003, "Seat not existed"),
    PLANE_EXISTED(1001, "Plane existed"),
    AIRLINE_NOT_EXISTED(1003, "Airline not existed"),
    AIRLINE_EXISTED(1003, "Airline existed"),
    CITY_NOT_EXISTED(1003, "City not existed"),
    CITY_EXISTED(1003, "City existed"),
    FLIGHT_SEAT_NOT_EXISTED(1003, "Flight seat not existed"),
    FLIGHT_AIRPORT_NOT_EXISTED(1003, "Flight airport not existed"),
    USERNAME_IS_REQUIRED(1003, "Username is required"),
    USERNAME_INVALID(1003, "Username must be at least 4 characters and max 20 characters"),
    PASSWORD_INVALID(1003, "Password must be at least 6 characters and max 50 characters"),
    PASSWORD_IS_REQUIRED(1003, "Password is required"),
    EMAIL_IS_REQUIRED(1003, "Email must not be blank"),
    EMAIL_INVALID(1003, "Invalid email format"),
    FULL_NAME_IS_REQUIRED(1003, "Full name must not be blank"),
    FULL_NAME_INVALID(1003, "Full name must be at most 50 characters"),
    PHONE_IS_REQUIRED(1003, "Phone number must not be blank"),
    PHONE_INVALID(1003, "Invalid phone number format"),
    ROLE_IS_REQUIRED(1003, "Role must not be null"),
    ROLE_INVALID(1003, "Role must be a positive number"),
    FLIGHT_NOT_FOUND(1003, "Flight not found"),
    TICKET_NOT_FOUND(1003, "Ticket not found"),
    CITY_IS_REQUIRED(1003, "City code is required"),
    AIRLINE_CODE_IS_REQUIRED(1003, "Airline code is required"),
    AIRLINE_CODE_INVALID(1003, "Airline code must be 2–3 characters"),
    AIRLINE_NAME_IS_REQUIRED(1003, "Airline name is required"),
    AIRLINE_NAME_INVALID(1003, "Airline name must be at most 30 characters"),
    PLANE_CODE_IS_REQUIRED(1003, "Plane code is required"),
    PLANE_CODE_INVALID(1003, "Plane code must be 3–10 characters"),
    PLANE_NAME_IS_REQUIRED(1003, "Plane name is required"),
    PLANE_NAME_INVALID(1003, "Plane name must be at most 50 characters"),
    AIRLINE_ID_IS_REQUIRED(1003, "Airline ID is required"),
    SEAT_CODE_IS_REQUIRED(1003, "Seat code is required"),
    SEAT_CODE_INVALID(1003, "Seat code must be 1–10 characters"),
    SEAT_NAME_IS_REQUIRED(1003, "Seat name is required"),
    SEAT_NAME_INVALID(1003, "Seat name must be at most 50 characters"),
    PRICE_IS_REQUIRED(1003, "Price is required"),
    PRICE_MUST_BE_POSITIVE(1003, "Price must be zero or greater"),
    DESCRIPTION_TOO_LONG(1003, "Description must be at most 200 characters"),
    FLIGHT_ID_IS_REQUIRED(1003, "Flight ID is required"),
    SEAT_ID_IS_REQUIRED(1003, "Seat ID is required"),
    PASSENGER_NAME_IS_REQUIRED(1003, "Passenger name is required"),
    PASSENGER_NAME_INVALID(1003, "Passenger name must be at most 100 characters"),
    PASSENGER_PHONE_IS_REQUIRED(1003, "Passenger phone is required"),
    PASSENGER_PHONE_INVALID(1003, "Passenger phone format is invalid"),
    PASSENGER_ID_CARD_IS_REQUIRED(1003, "Passenger ID card is required"),
    PASSENGER_ID_CARD_INVALID(1003, "Passenger ID card format is invalid"),
    PASSENGER_EMAIL_IS_REQUIRED(1003, "Passenger email is required"),
    PASSENGER_EMAIL_INVALID(1003, "Passenger email format is invalid"),
    BAGGAGE_STATUS_IS_REQUIRED(1003, "Baggage status is required"),
    FLIGHT_CODE_IS_REQUIRED(1003, "Flight code is required"),
    FLIGHT_CODE_INVALID(1003, "Flight code format is invalid"),
    PLANE_ID_IS_REQUIRED(1003, "Plane ID is required"),
    DEPARTURE_AIRPORT_ID_IS_REQUIRED(1003, "Departure airport ID is required"),
    ARRIVAL_AIRPORT_ID_IS_REQUIRED(1003, "Arrival airport ID is required"),
    DEPARTURE_TIME_INVALID(1003, "Departure time is required and must match format"),
    ARRIVAL_TIME_INVALID(1003, "Arrival time is required and must match format"),
    ORIGIN_PRICE_INVALID(1003, "Origin price must be provided and >= 0"),
    CODE_SIZE_INVALID(1003, "Code must be between 2 and 10 characters"),
    CODE_FORMAT_INVALID(1003, "Code must be uppercase letters and/or digits"),
    NAME_IS_REQUIRED(1003, "Name is required"),
    NAME_SIZE_INVALID(1003, "Name must not exceed 100 characters"),
    NAME_FORMAT_INVALID(1003, "Name must contain only letters and spaces");

    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

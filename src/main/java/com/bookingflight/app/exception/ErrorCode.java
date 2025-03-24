package com.bookingflight.app.exception;

public enum ErrorCode {
    USER_EXISTED(1001, "User existed"),
    USER_NOT_EXISTED(1003, "User not existed"),
    UNIDENTIFIED_EXCEPTION(9999, "Unidentified exception"),
    PASSWORD_INVALID(1002, "Password must be at least 3 characters"),
    INCORRECT_PASSWORD(1004, "Incorrect password"),
    AIRPORT_EXISTED(1001, "Airport's name existed"),
    AIRPORT_NOT_EXISTED(1003, "Airport not existed"),
    EMAIL_EXISTED(1001, "Email existed"),
    FLIGHT_EXISTED(1001, "Flight's number existed"),
    FLIGHT_NOT_EXISTED(1003, "Flight not existed"),
    BOOKING_EXISTED(1001, "Booking's id existed"),
    BOOKING_NOT_EXISTED(1003, "Booking not existed"),
    ACCOUNT_NOT_EXISTED(1003, "Account not existed"),
    PLANE_NOT_EXISTED(1003, "Plane not existed"),
    CUSTOMER_NOT_EXISTED(1003, "Customer not existed"),
    TICKET_EXISTED(1001, "Ticket number existed"),
    TICKET_NOT_EXISTED(1003, "Ticket number not existed"),
    SEAT_EXISTED(1001, "Seat existed"),
    SEAT_NOT_EXISTED(1003, "Seat not existed"),
    PLANE_EXISTED(1001, "Plane existed"),;

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

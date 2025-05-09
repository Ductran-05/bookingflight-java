package com.bookingflight.app.domain;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Flight")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String flightCode;

    @ManyToOne
    @JoinColumn(name = "planeId")
    Plane plane;
    @ManyToOne
    @JoinColumn(name = "departureAirportId")
    Airport departureAirport;
    @ManyToOne
    @JoinColumn(name = "arrivalAirportId")
    Airport arrivalAirport;
    Date departureTime;
    Date arrivalTime;
    Number originPrice;
}

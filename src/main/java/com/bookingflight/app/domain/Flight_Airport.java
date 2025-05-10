package com.bookingflight.app.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Flight_Airport")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight_Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @ManyToOne
    @JoinColumn(name = "flightId")
    Flight flight;
    @ManyToOne
    @JoinColumn(name = "airportId")
    Airport airport;
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime departureTime;

    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    LocalDateTime arrivalTime;

    String note;
}

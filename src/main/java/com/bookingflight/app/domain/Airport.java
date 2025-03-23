package com.bookingflight.app.domain;

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
@Table(name = "Airport")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String airportCode;
    String airportName;

    @ManyToOne
    @JoinColumn(name = "cityId")
    City city;
}

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Plane")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String planeCode;
    String planeName;
    @ManyToOne
    @JoinColumn(name = "airlineId")
    Airline airline;
    @Builder.Default
    Boolean canUpdate = true;
    @Builder.Default
    Boolean canDelete = true;
}

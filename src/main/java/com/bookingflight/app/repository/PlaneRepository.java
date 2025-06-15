package com.bookingflight.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bookingflight.app.domain.Airline;
import com.bookingflight.app.domain.Plane;

@Repository
public interface PlaneRepository extends JpaRepository<Plane, String>, JpaSpecificationExecutor<Plane> {

    Plane findByPlaneCode(String planeCode);

    Plane[] findAllByAirline(Airline airline);

}

package com.bookingflight.app.repository;

import com.bookingflight.app.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, String> {
    Optional<City> findByCityCode(String cityCode);

    boolean existsByCityCode(String cityCode);
}

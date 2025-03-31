package com.tecvinson.location.repositories;

import com.tecvinson.location.entities.Area;
import com.tecvinson.location.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {
    @Query("SELECT ar FROM Area ar WHERE LOWER(ar.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Area> getByNameIgnoringCase(String name);
    List<Area> getByCity(City city);
    boolean existsByNameAndCityId(String name, UUID cityId);
}

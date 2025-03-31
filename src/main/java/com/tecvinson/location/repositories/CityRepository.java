package com.tecvinson.location.repositories;

import com.tecvinson.location.entities.City;
import com.tecvinson.location.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<City, UUID> {
    @Query("SELECT c FROM City c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<City> getByNameIgnoringCase(String name);
    List<City> getByState(State state);
    boolean existsByNameAndStateId(String name, UUID StateId);
}

package com.tecvinson.location.repositories;

import com.tecvinson.location.entities.Country;
import com.tecvinson.location.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StateRepository extends JpaRepository<State, UUID> {
    @Query("SELECT s FROM State s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<State> getByNameIgnoringCase(String name);
    List<State> getByCountry(Country country);
    boolean existsByNameAndCountryId(String name, UUID countryId);
}

package com.tecvinson.location.repositories;

import com.tecvinson.location.entities.Continent;
import com.tecvinson.location.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
    @Query("SELECT c FROM Country c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Country> getByNameIgnoringCase(String name);
    List<Country> getByContinent(Continent continent);
}

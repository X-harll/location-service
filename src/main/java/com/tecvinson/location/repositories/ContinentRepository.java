package com.tecvinson.location.repositories;

import com.tecvinson.location.entities.Continent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContinentRepository extends JpaRepository<Continent, UUID> {
    @Query("SELECT c FROM Continent c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Continent> getByNameIgnoringCase(String name);
}
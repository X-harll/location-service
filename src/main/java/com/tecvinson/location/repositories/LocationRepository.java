package com.tecvinson.location.repositories;

import com.tecvinson.location.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    @Query("SELECT l FROM Location l WHERE LOWER(l.houseAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(l.streetName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Location> findByHouseAddressOrStreetName(String searchTerm);
}

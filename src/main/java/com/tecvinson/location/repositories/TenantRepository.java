package com.tecvinson.location.repositories;

import com.tecvinson.location.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository <Tenant, UUID> {
    Optional<Tenant> findByApiKey(String apiKey);
    Optional<Tenant> findByEmail(String email);
}

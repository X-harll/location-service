package com.tecvinson.location.repositories;

import com.tecvinson.location.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository <Client, UUID> {
    boolean existsByNameAndTenantId(String name, UUID tenantId);
}

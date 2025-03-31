package com.tecvinson.location.dtos.client;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClientResponse {
   private UUID id;
   private String name;
   private String tenantName;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

}

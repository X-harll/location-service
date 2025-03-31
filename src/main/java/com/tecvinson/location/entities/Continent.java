package com.tecvinson.location.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Continents")
public class Continent extends CommonFields {

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    private UUID clientId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}

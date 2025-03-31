package com.tecvinson.location.dtos.state;

import java.time.LocalDateTime;
import java.util.UUID;


public class StateResponse {
    private UUID id;
    private String name;
    private String countryName;


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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}

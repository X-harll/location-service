package com.tecvinson.location.dtos.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.util.UUID;


public class CityRequest {

    @NotBlank(message = "City name cannot be blank")
    @Size(max = 60, message = "City name must not exceed 60 characters")
    private String name;

    @NotNull(message = "You must provide a State ID")
    private UUID stateId;



    public @NotBlank(message = "City name cannot be blank") @Size(max = 60, message = "City name must not exceed 60 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "City name cannot be blank") @Size(max = 60, message = "City name must not exceed 60 characters") String name) {
        this.name = name;
    }

    public @NotNull(message = "You must provide a State ID") UUID getStateId() {
        return stateId;
    }

    public void setStateId(@NotNull(message = "You must provide a State ID") UUID stateId) {
        this.stateId = stateId;
    }

}

package com.tecvinson.location.dtos.continent;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateContinentRequest {
    @NotBlank(message = "Continent name cannot be blank")
    @Size(max = 60, message = "Continent name must not exceed 60 characters")
    private String name;

    public @NotBlank(message = "Continent name cannot be blank") @Size(max = 60, message = "Continent name must not exceed 60 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Continent name cannot be blank") @Size(max = 60, message = "Continent name must not exceed 60 characters") String name) {
        this.name = name;
    }
}

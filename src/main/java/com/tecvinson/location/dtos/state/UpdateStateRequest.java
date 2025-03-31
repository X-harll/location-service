package com.tecvinson.location.dtos.state;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class UpdateStateRequest {


    @NotBlank(message = "State name cannot be blank")
    @Size(max = 60, message = "State name must not exceed 60 characters")
    private String name;

    @NotNull(message = "You must provide a country ID")
    private UUID countryId;



    public @NotBlank(message = "State name cannot be blank") @Size(max = 60, message = "State name must not exceed 60 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "State name cannot be blank") @Size(max = 60, message = "State name must not exceed 60 characters") String name) {
        this.name = name;
    }

    public @NotNull(message = "You must provide a country ID") UUID getCountryId() {
        return countryId;
    }

    public void setCountryId(@NotNull(message = "You must provide a country ID") UUID countryId) {
        this.countryId = countryId;
    }

}

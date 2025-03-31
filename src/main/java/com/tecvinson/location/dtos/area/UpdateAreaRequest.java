package com.tecvinson.location.dtos.area;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;


public class UpdateAreaRequest {

    @NotBlank(message = "Area name cannot be blank")
    @Size(max = 60, message = "area name must not exceed 60 characters")
    private String name;

    @NotNull(message = "You must provide a City ID")
    private UUID cityId;




    public @NotBlank(message = "Area name cannot be blank") @Size(max = 60, message = "area name must not exceed 60 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Area name cannot be blank") @Size(max = 60, message = "area name must not exceed 60 characters") String name) {
        this.name = name;
    }

    public @NotNull(message = "You must provide a City ID") UUID getCityId() {
        return cityId;
    }

    public void setCityId(@NotNull(message = "You must provide a City ID") UUID cityId) {
        this.cityId = cityId;
    }

}

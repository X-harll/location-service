package com.tecvinson.location.dtos.country;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class CountryRequest {

    @NotNull(message = "You must provide a Continent ID")
    private UUID continentId;

    @NotBlank(message = "Country name cannot be blank")
    @Size(max = 60, message = "Country name must not exceed 60 characters")
    private String name;

    @NotBlank(message = "Country code cannot be blank")
    @Size(max = 5, message = "Country code must not exceed 5 characters")
    private String countryCode;

    @NotBlank(message = "Phone code cannot be blank")
    @Size(max = 5, message = "Phone code must not exceed 5 characters")
    private String phoneCode;

    @NotBlank
    private String flag;



    public @NotNull(message = "You must provide a Continent ID") UUID getContinentId() {
        return continentId;
    }

    public void setContinentId(@NotNull(message = "You must provide a Continent ID") UUID continentId) {
        this.continentId = continentId;
    }

    public @NotBlank(message = "Country name cannot be blank") @Size(max = 60, message = "Country name must not exceed 60 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Country name cannot be blank") @Size(max = 60, message = "Country name must not exceed 60 characters") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Country code cannot be blank") @Size(max = 5, message = "Country code must not exceed 5 characters") String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(@NotBlank(message = "Country code cannot be blank") @Size(max = 5, message = "Country code must not exceed 5 characters") String countryCode) {
        this.countryCode = countryCode;
    }

    public @NotBlank(message = "Phone code cannot be blank") @Size(max = 5, message = "Phone code must not exceed 5 characters") String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(@NotBlank(message = "Phone code cannot be blank") @Size(max = 5, message = "Phone code must not exceed 5 characters") String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public @NotBlank String getFlag() {
        return flag;
    }

    public void setFlag(@NotBlank String flag) {
        this.flag = flag;
    }
}


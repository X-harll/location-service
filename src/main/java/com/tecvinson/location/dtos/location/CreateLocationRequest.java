package com.tecvinson.location.dtos.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateLocationRequest {
    @NotBlank(message = "House Address Cannot be blank")
    @Size(max = 20, message = "House Address must not exceed 60 characters")
    private String houseAddress;

    @NotBlank(message = "Street Name Cannot be blank")
    @Size(max = 60, message = "Street Name must not exceed 60 characters")
    private String streetName;

    @Size(max = 140, message = "Must not exceed 140 characters")
    private String freeText;

    @NotNull(message = "Enter the latitude")
    @Size(max = 11, message = "Latitude must not exceed 11 characters")
    private long latitude;

    @NotNull(message = "Enter the longitude")
    @Size(max = 11, message = "Longitude must not exceed 11 characters")
    private long longitude;

    @NotNull(message = "You must provide an Area ID")
    private UUID areaId;




    public @NotNull(message = "You must provide an Area ID") UUID getAreaId() {
        return areaId;
    }

    public void setAreaId(@NotNull(message = "You must provide an Area ID") UUID areaId) {
        this.areaId = areaId;
    }

    @NotNull(message = "Enter the longitude")
    @Size(max = 11, message = "Longitude must not exceed 11 characters")
    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotNull(message = "Enter the longitude") @Size(max = 11, message = "Longitude must not exceed 11 characters") long longitude) {
        this.longitude = longitude;
    }

    @NotNull(message = "Enter the latitude")
    @Size(max = 11, message = "Latitude must not exceed 11 characters")
    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull(message = "Enter the latitude") @Size(max = 11, message = "Latitude must not exceed 11 characters") long latitude) {
        this.latitude = latitude;
    }

    public @Size(max = 140, message = "Must not exceed 140 characters") String getFreeText() {
        return freeText;
    }

    public void setFreeText(@Size(max = 140, message = "Must not exceed 140 characters") String freeText) {
        this.freeText = freeText;
    }

    public @NotBlank(message = "Street Name Cannot be blank") @Size(max = 60, message = "Street Name must not exceed 60 characters") String getStreetName() {
        return streetName;
    }

    public void setStreetName(@NotBlank(message = "Street Name Cannot be blank") @Size(max = 60, message = "Street Name must not exceed 60 characters") String streetName) {
        this.streetName = streetName;
    }

    public @NotBlank(message = "House Address Cannot be blank") @Size(max = 20, message = "House Address must not exceed 60 characters") String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(@NotBlank(message = "House Address Cannot be blank") @Size(max = 20, message = "House Address must not exceed 60 characters") String houseAddress) {
        this.houseAddress = houseAddress;
    }
}

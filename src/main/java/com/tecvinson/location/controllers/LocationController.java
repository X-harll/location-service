package com.tecvinson.location.controllers;

import com.tecvinson.location.dtos.location.CreateLocationRequest;
import com.tecvinson.location.dtos.location.LocationResponse;
import com.tecvinson.location.dtos.location.UpdateLocationRequest;
import com.tecvinson.location.services.LocationService;
import com.tecvinson.location.services.ValidationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/locations")
@Tag(name = "Address Management")
public class LocationController {

    @Autowired
    LocationService locationService;
    @Autowired
    ValidationService validationService;


    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(
            @RequestHeader("X-API-KEY") String apiKey,
            @Valid @RequestBody CreateLocationRequest locationRequest) {

        validationService.validateApiKey(apiKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(locationRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(
            @RequestHeader("X-API-KEY") String apiKey,
            @PathVariable UUID id, @Valid @RequestBody UpdateLocationRequest locationRequest
            ) {

        validationService.validateApiKey(apiKey);

        return ResponseEntity.ok(locationService.updateLocation(id, locationRequest));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getLocations() {
        return ResponseEntity.ok(locationService.getLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable UUID id) {
        return ResponseEntity.ok(locationService.getLocation(id));
    }


    @GetMapping("/getbycountry/{countryId}")
    public ResponseEntity<List<LocationResponse>> getLocationsByCountry(@PathVariable UUID countryId) {
        return ResponseEntity.ok(locationService.getLocationByCountry(countryId));
    }

    @GetMapping("/getbystate/{stateId}")
    public ResponseEntity<List<LocationResponse>> getLocationsByState(@PathVariable UUID stateId) {
        return ResponseEntity.ok(locationService.getLocationByState(stateId));
    }

    @GetMapping("/getbycity/{cityId}")
    public ResponseEntity<List<LocationResponse>> getLocationsByCity(@PathVariable UUID cityId) {
        return ResponseEntity.ok(locationService.getLocationByCity(cityId));
    }

    @GetMapping("/getbyarea/{areaId}")
    public ResponseEntity<List<LocationResponse>> getLocationsByArea(@PathVariable UUID areaId) {
        return ResponseEntity.ok(locationService.getLocationByArea(areaId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationResponse>> getBySearch(@RequestParam String searchTerm) {
        return ResponseEntity.ok(locationService.getBySearch(searchTerm));
    }

}

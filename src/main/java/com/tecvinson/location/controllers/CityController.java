package com.tecvinson.location.controllers;

import com.tecvinson.location.dtos.city.CityResponse;
import com.tecvinson.location.dtos.city.CityRequest;
import com.tecvinson.location.services.CityService;
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
@RequestMapping("/cities")
@Tag(name = "City Management")
public class CityController {

    @Autowired
    CityService cityService;
    @Autowired
    ValidationService validationService;

    @PostMapping
    public ResponseEntity<CityResponse> createCity(
            @RequestHeader("X-API-KEY") String apiKey,
            @Valid @RequestBody CityRequest cityRequest) {

        validationService.validateApiKey(apiKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.createCity(cityRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> updateCity(
            @RequestHeader("X-API-KEY") String apiKey,
            @PathVariable UUID id, @Valid @RequestBody CityRequest cityRequest
            ) {

        validationService.validateApiKey(apiKey);

        return ResponseEntity.ok(cityService.updateCity(id, cityRequest));
    }

    @GetMapping
    public ResponseEntity<List<CityResponse>> getCities() {
        return ResponseEntity.ok(cityService.getCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCity(@PathVariable UUID id){
        return ResponseEntity.ok(cityService.getCity(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CityResponse>> getCityByName( @RequestParam String name) {
        return ResponseEntity.ok(cityService.getByName(name));
    }

    @GetMapping("/getbystate/{stateId}")
    public ResponseEntity<List<CityResponse>> getByState(@PathVariable UUID stateId) {
        return ResponseEntity.ok(cityService.getByState(stateId));
    }

    @GetMapping("/getbycountry/{countryId}")
    public ResponseEntity<List<CityResponse>> getByCountry(@PathVariable UUID countryId) {
        return ResponseEntity.ok(cityService.getByCountry(countryId));
    }


}

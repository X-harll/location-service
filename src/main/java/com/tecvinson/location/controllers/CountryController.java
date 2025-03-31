package com.tecvinson.location.controllers;

import com.tecvinson.location.dtos.country.CountryResponse;
import com.tecvinson.location.dtos.country.CountryRequest;
import com.tecvinson.location.services.CountryService;
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
@RequestMapping("/countries")
@Tag(name = "Country Management")
public class CountryController {

    @Autowired
    CountryService countryService;
    @Autowired
    ValidationService validationService;



    @PostMapping
    public ResponseEntity<CountryResponse> createCountry(
            @RequestHeader("X-API-KEY") String apiKey,
            @Valid @RequestBody CountryRequest countryRequest)
    {
        validationService.validateApiKey(apiKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.createCountry(countryRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> updateCountry (
            @RequestHeader("X-API-KEY") String apiKey,
            @PathVariable UUID id, @Valid @RequestBody  CountryRequest countryRequest)
    {
        validationService.validateApiKey(apiKey);
        return ResponseEntity.ok(countryService.updateCountry(id, countryRequest));
    }

    @GetMapping()
    public ResponseEntity<List<CountryResponse>> getCountries() {
        return ResponseEntity.ok(countryService.getCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> getCountry( @PathVariable UUID id ) {
        return ResponseEntity.ok(countryService.getCountry(id));
    }


    @GetMapping("/search")
    public ResponseEntity<List<CountryResponse>> getCountriesByName(@RequestParam String name) {
        return ResponseEntity.ok(countryService.getCountriesByName(name));
    }

    @GetMapping("/getbycontinent/{continentId}")
    public ResponseEntity<List<CountryResponse>> getByContinent(@PathVariable UUID continentId) {
        return ResponseEntity.ok(countryService.getByContinent(continentId));
    }

}

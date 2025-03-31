package com.tecvinson.location.controllers;

import com.tecvinson.location.dtos.continent.ContinentResponse;
import com.tecvinson.location.dtos.continent.CreateContinentRequest;
import com.tecvinson.location.dtos.continent.UpdateContinentRequest;
import com.tecvinson.location.services.ContinentService;
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
@RequestMapping("/continents")
@Tag(name = "Continent Management")
public class ContinentController {

    @Autowired
    ContinentService continentService;

    @Autowired
    ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<ContinentResponse> createContinent(
            @RequestHeader("X-API-KEY") String apiKey, @RequestHeader("CLIENT-ID") UUID clientId,
            @Valid @RequestBody CreateContinentRequest continentRequest
            ) {
        validationService.validateApiKey(apiKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(continentService.createContinent(clientId,continentRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContinentResponse> updateContinent(
            @RequestHeader("X-API-KEY") String apiKey, @PathVariable UUID id
            , @Valid @RequestBody UpdateContinentRequest continentRequest
            ) {

        validationService.validateApiKey(apiKey);

        return ResponseEntity.ok(continentService.updateContinent(id,continentRequest));
    }

    @GetMapping
    public ResponseEntity<List<ContinentResponse>> getContinents(){
        return ResponseEntity.ok(continentService.getContinents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContinentResponse> getContinent(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(continentService.getContinent(id));
    }
}

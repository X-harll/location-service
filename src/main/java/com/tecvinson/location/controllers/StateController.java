package com.tecvinson.location.controllers;

import com.tecvinson.location.dtos.state.CreateStateRequest;
import com.tecvinson.location.dtos.state.StateResponse;
import com.tecvinson.location.dtos.state.UpdateStateRequest;
import com.tecvinson.location.services.StateService;
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
@RequestMapping("/states")
@Tag(name = "State Management")
public class StateController {

    @Autowired
    StateService stateService;
    @Autowired
    ValidationService validationService;


    @PostMapping
    public ResponseEntity<StateResponse> createState (
            @RequestHeader("X-API-KEY") String apiKey,
            @Valid @RequestBody CreateStateRequest createStateRequest
    ) {
        validationService.validateApiKey(apiKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(stateService.createState(createStateRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateResponse> updateState (
            @RequestHeader("X-API-KEY") String apiKey,
            @PathVariable UUID id, @Valid @RequestBody UpdateStateRequest stateRequest
            ) {
        validationService.validateApiKey(apiKey);

        return ResponseEntity.ok(stateService.updateState(id, stateRequest));
    }


    @GetMapping
    public ResponseEntity<List<StateResponse>> getStates() {
        return ResponseEntity.ok(stateService.getStates());
    }


    @GetMapping("/{id}")
    public  ResponseEntity<StateResponse> getState(@PathVariable UUID id) {
        return  ResponseEntity.ok(stateService.getState(id));
    }

    @GetMapping("/getbycountry/{countryId}")
    public ResponseEntity<List<StateResponse>> getByCountryId (@PathVariable UUID countryId) {
        return ResponseEntity.ok(stateService.getByCountryId(countryId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StateResponse>> getStatesByName(@RequestParam String name) {
        return ResponseEntity.ok(stateService.getStatesByName(name));
    }

}

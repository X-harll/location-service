package com.tecvinson.location.controllers;

import com.tecvinson.location.dtos.area.AreaResponse;
import com.tecvinson.location.dtos.area.CreateAreaRequest;
import com.tecvinson.location.dtos.area.UpdateAreaRequest;
import com.tecvinson.location.services.AreaService;
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
@RequestMapping("/areas")
@Tag(name = "Area Management")
public class AreaController {

    @Autowired
    AreaService areaService;
    @Autowired
    ValidationService validationService;

    @PostMapping("/")
    public ResponseEntity<AreaResponse> createArea(
            @RequestHeader("X-API-KEY") String apiKey,
            @Valid @RequestBody CreateAreaRequest areaRequest) {

        validationService.validateApiKey(apiKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(areaService.createArea(areaRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaResponse> updateArea(
            @RequestHeader("X-API-KEY") String apiKey,
            @PathVariable UUID id, @Valid @RequestBody UpdateAreaRequest areaRequest) {

        validationService.validateApiKey(apiKey);

        return ResponseEntity.ok(areaService.updateArea(id, areaRequest));
    }

    @GetMapping
    public ResponseEntity<List<AreaResponse>> getAreas() {
        return ResponseEntity.ok(areaService.getAreas());
    }


    @GetMapping("/{id}")
    public ResponseEntity<AreaResponse> getArea(@PathVariable UUID id) {
        return ResponseEntity.ok(areaService.getArea(id));
    }


    @GetMapping("/search")
    public ResponseEntity<List<AreaResponse>> getAreaByName(@RequestParam String name) {
        return ResponseEntity.ok(areaService.getByName(name));
    }

    @GetMapping("/getbycity/{cityId}")
    public ResponseEntity<List<AreaResponse>> getByCity(@PathVariable UUID cityId) {
        return ResponseEntity.ok(areaService.getByCity(cityId));
    }

    @GetMapping("/getbystate/{stateId}")
    public ResponseEntity<List<AreaResponse>> getByState(@PathVariable UUID stateId) {
        return ResponseEntity.ok(areaService.getByState(stateId));
    }

    @GetMapping("/getbycountry/{countryId}")
    public ResponseEntity<List<AreaResponse>> getByCountry(@PathVariable UUID countryId) {
        return ResponseEntity.ok(areaService.getByCountry(countryId));
    }

}

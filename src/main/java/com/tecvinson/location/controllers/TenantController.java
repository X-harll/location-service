package com.tecvinson.location.controllers;

import com.tecvinson.location.dtos.tenant.CreateTenantRequest;
import com.tecvinson.location.dtos.tenant.TenantResponse;
import com.tecvinson.location.dtos.tenant.UpdateTenantRequest;
import com.tecvinson.location.services.TenantService;
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
@RequestMapping("/tenants")
@Tag(name = "Tenant Management")
public class TenantController {

    @Autowired
    TenantService tenantService;
    @Autowired
    ValidationService validationService;

    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(
            @RequestHeader("X-API-KEY") String apiKey,
            @Valid @RequestBody CreateTenantRequest tenantRequest) {
        validationService.validateAdminApiKey(apiKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.createTenant(tenantRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> updateTenant(
            @RequestHeader("X-API-KEY") String apiKey,
            @PathVariable UUID id, @Valid @RequestBody UpdateTenantRequest tenantRequest) {
        validationService.validateAdminApiKey(apiKey);
        return ResponseEntity.ok(tenantService.updateTenant(id, tenantRequest));
    }

    @GetMapping
    public ResponseEntity<List<TenantResponse>>getTenants(
    ) {
        return ResponseEntity.ok(tenantService.getTenants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getTenant( @PathVariable UUID id) {
        return ResponseEntity.ok(tenantService.getTenant(id));
    }
}

package com.tecvinson.location.services;

import com.tecvinson.location.dtos.tenant.CreateTenantRequest;
import com.tecvinson.location.dtos.tenant.TenantResponse;
import com.tecvinson.location.dtos.tenant.UpdateTenantRequest;
import com.tecvinson.location.entities.Tenant;
import com.tecvinson.location.exceptions.NotFoundException;
import com.tecvinson.location.repositories.TenantRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TenantService {

    // Logger instance for tracking activities in the service
    private static final Logger logger = LoggerFactory.getLogger(TenantService.class);

    private final TenantRepository tenantRepository; // Repository for database operations
    private final ModelMapper modelMapper; // Used for mapping between DTOs and entities
    private final ValidationService validationService;
    private final EncryptionService encryptionService;

    // Constructor-based dependency injection for the repository and model mapper
    public TenantService(TenantRepository tenantRepository, ModelMapper modelMapper, ValidationService validationService, EncryptionService encryptionService) {
        this.tenantRepository = tenantRepository;
        this.modelMapper = modelMapper;
        this.validationService = validationService;
        this.encryptionService = encryptionService;
    }

    /**
     * Creates a new tenant in the system.
     * @param tenantRequest Data required to create a new tenant (from the DTO).
     * @return The response containing the created tenant's information.
     */
    @Transactional
    public TenantResponse createTenant(CreateTenantRequest tenantRequest) {
        logger.info("Creating a tenant");

        // Validate Email
        validationService.validateEmail(tenantRequest.getEmail());

        // Generate and process API key
        String generatedApiKey = UUID.randomUUID().toString();
        String hashedApiKey = validationService.hashApiKey(generatedApiKey);
        String encryptedApiKey = encryptionService.encrypt(generatedApiKey); // Encrypt using configured key

        // Map DTO to Entity
        Tenant tenant = modelMapper.map(tenantRequest, Tenant.class);
        tenant.setApiKey(hashedApiKey);
        tenant.setEncryptedApiKey(encryptedApiKey); // Store encrypted API key
        tenant.setActive(true);
        tenant.setCreatedBy("SYSTEM");
        tenant.setModifiedBy("SYSTEM");

        // Save to Database
        tenant = tenantRepository.save(tenant);
        logger.info("Tenant with ID: {} created", tenant.getId());

        // Map to Response DTO (return original API key just once)
        TenantResponse tenantResponse = modelMapper.map(tenant, TenantResponse.class);
        tenantResponse.setApiKey(generatedApiKey);
        return tenantResponse;
    }





    /**
     * Updates an existing tenant's information.
     * @param id The ID of the tenant to be updated.
     * @param tenantRequest Data required to update the tenant (from the DTO).
     * @return The updated tenant's information as a response DTO.
     */
    @Transactional
    public TenantResponse updateTenant(UUID id, UpdateTenantRequest tenantRequest) {
        logger.info("Updating tenant with ID: {}", id);

        // Find the tenant by ID or throw an exception if not found
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant not found"));

        //Check if Email is Valid
        validationService.validateEmail(tenantRequest.getEmail());

        // Update tenant fields with data from the request DTO
        tenant.setEmail(tenantRequest.getEmail());
        tenant.setActive(tenantRequest.isActive());
        tenant.setCreatedBy("SYSTEM");
        tenant.setModifiedBy("SYSTEM");

        logger.info("Tenant updated. Email: {}, Status: {}", tenantRequest.getEmail(), tenantRequest.isActive());

        // Save the updated tenant and map it to a response DTO
        return modelMapper.map(tenantRepository.save(tenant), TenantResponse.class);
    }

    /**
     * Retrieves all tenants in the system.
     * @return A list of tenant response DTOs.
     */
    public List<TenantResponse> getTenants() {
        logger.info("Retrieving all tenants");

        // Retrieve all tenants from the database
        List<Tenant> tenants = tenantRepository.findAll();
        if (tenants.isEmpty()) {
            logger.warn("No tenants found in the database");
            return Collections.emptyList();
        } else {
            logger.info("Found {} tenants", tenants.size());
        }

        // Map and decrypt API keys
        return tenants.stream()
                .map(tenant -> {
                    TenantResponse response = modelMapper.map(tenant, TenantResponse.class);
                    if (tenant.getEncryptedApiKey() != null) {
                        response.setApiKey(encryptionService.decrypt(tenant.getEncryptedApiKey())); // Decrypt before returning
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }


    /**
     * Retrieves a specific tenant by ID.
     * @param id The ID of the tenant to retrieve.
     * @return The tenant's information as a response DTO.
     */
    public TenantResponse getTenant(UUID id) {
        logger.info("Retrieving tenant with ID: {}", id);

        // Find the tenant by ID or throw an exception if not found
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tenant not found"));

        // Map the tenant entity to a response DTO
        TenantResponse response = modelMapper.map(tenant, TenantResponse.class);

        // Decrypt API key before returning it
        if (tenant.getEncryptedApiKey() != null) {
            response.setApiKey(encryptionService.decrypt(tenant.getEncryptedApiKey()));
        }

        return response;
    }


}

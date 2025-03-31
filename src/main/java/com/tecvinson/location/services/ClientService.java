package com.tecvinson.location.services;

import com.tecvinson.location.dtos.client.ClientResponse;
import com.tecvinson.location.dtos.client.ClientRequest;
import com.tecvinson.location.entities.Client;
import com.tecvinson.location.entities.Tenant;
import com.tecvinson.location.exceptions.NotFoundException;
import com.tecvinson.location.exceptions.ResourceConflictException;
import com.tecvinson.location.repositories.ClientRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {

    // Logger instance for tracking service operations
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ModelMapper modelMapper; // Used for DTO to Entity mapping and vice versa
    private final ClientRepository clientRepository; // Repository for client-related database operations
    private final ValidationService validationService; // Service to validate API keys and tenants

    // Constructor for dependency injection
    public ClientService(ModelMapper modelMapper, ClientRepository clientRepository, ValidationService validationService) {
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
        this.validationService = validationService;
    }

    /**
     * Creates a new client for a specific tenant.
     * @param apiKey The API key of the tenant.
     * @param clientRequest The client creation data from the request DTO.
     * @return The response containing the created client's information.
     */
    @Transactional
    public ClientResponse createClient(String apiKey, ClientRequest clientRequest) {
        logger.info("Creating a Client");

        // Validate the API key and retrieve the associated tenant
        Tenant tenant = validationService.validateApiKey(apiKey);

        // Check if a client with the same name already exists for this tenant
        boolean clientExist = clientRepository.existsByNameAndTenantId(clientRequest.getName(), tenant.getId());
        if (clientExist) {
            throw new ResourceConflictException("Client exists for this Tenant");
        }

        // Map the request DTO to a Client entity and set additional fields
        Client client = modelMapper.map(clientRequest, Client.class);
        client.setTenant(tenant);
        client.setCreatedBy("SYSTEM");
        client.setModifiedBy("SYSTEM");

        // Save the client in the database
        client = clientRepository.save(client);

        logger.info("Client Created. Name: {}", client.getName());

        // Map the saved entity to a response DTO
        return modelMapper.map(client, ClientResponse.class);
    }

    /**
     * Updates an existing client's details.
     * @param apiKey The API key of the tenant.
     * @param id The ID of the client to update.
     * @param clientRequest The client update data from the request DTO.
     * @return The updated client's information as a response DTO.
     */
    @Transactional
    public ClientResponse updateClient(String apiKey, UUID id, ClientRequest clientRequest) {
        logger.info("Updating the details of Client with ID: {}", id);

        // Validate the API key and retrieve the associated tenant
        Tenant tenant = validationService.validateApiKey(apiKey);

        // Check if a client with the same name already exists for this tenant
        boolean clientExist = clientRepository.existsByNameAndTenantId(clientRequest.getName(), tenant.getId());
        if (clientExist) {
            throw new ResourceConflictException("Client exists for this Tenant");
        }

        // Find the client by ID or throw an exception if not found
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client Not Found"));

        // Update the client details
        client.setName(clientRequest.getName());
        client.setTenant(tenant);

        logger.info("Client Updated. Name: {}", clientRequest.getName());

        // Save the updated client and map it to a response DTO
        return modelMapper.map(clientRepository.save(client), ClientResponse.class);
    }

    /**
     * Retrieves all clients in the system.
     * @return A list of client response DTOs.
     */
    public List<ClientResponse> getClients() {
        logger.info("Retrieving All Clients");

        // Retrieve all clients from the database
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            logger.warn("No Client found");
            return Collections.emptyList();
        } else {
            logger.info("Found {} Client(s)", clients.size());
        }

        // Map the list of clients to a list of response DTOs with tenant names
        return clients.stream()
                .map(client -> {
                    ClientResponse clientResponse = modelMapper.map(client, ClientResponse.class);
                    clientResponse.setTenantName(client.getTenant().getName());
                    return clientResponse;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific client by ID.
     * @param id The ID of the client to retrieve.
     * @return The client's information as a response DTO.
     */
    public ClientResponse getClient(UUID id) {
        logger.info("Retrieving Client with ID: {}", id);

        // Find the client by ID or throw an exception if not found
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client Not Found"));

        // Map the client entity to a response DTO
        return modelMapper.map(client, ClientResponse.class);
    }
}

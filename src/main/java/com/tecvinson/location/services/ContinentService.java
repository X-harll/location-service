package com.tecvinson.location.services;

import com.tecvinson.location.dtos.continent.ContinentResponse;
import com.tecvinson.location.dtos.continent.CreateContinentRequest;
import com.tecvinson.location.dtos.continent.UpdateContinentRequest;
import com.tecvinson.location.entities.Client;
import com.tecvinson.location.entities.Continent;
import com.tecvinson.location.exceptions.NotFoundException;
import com.tecvinson.location.repositories.ClientRepository;
import com.tecvinson.location.repositories.ContinentRepository;
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
public class ContinentService {

    // Logger for tracking service operations
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    private final ClientRepository clientRepository; // Repository for client-related database operations
    private final ContinentRepository continentRepository; // Repository for continent-related database operations
    private final ModelMapper modelMapper; // Used for mapping DTOs to entities and vice versa

    // Constructor for dependency injection
    public ContinentService(ClientRepository clientRepository, ContinentRepository continentRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.continentRepository = continentRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new continent and associates it with a client.
     * @param continentRequest The continent creation data from the request DTO.
     * @return The created continent's information as a response DTO.
     */
    @Transactional
    public ContinentResponse createContinent(UUID clientId, CreateContinentRequest continentRequest) {
        logger.info("Creating a Continent with name: {}", continentRequest.getName());

        // Validate that the client exists
        if (!clientRepository.existsById(clientId)) {
            throw new NotFoundException("Client Not Found");
        }

        // Map the request DTO to a Continent entity and set additional fields
        Continent continent = modelMapper.map(continentRequest, Continent.class);
        continent.setCreatedBy("SYSTEM");
        continent.setModifiedBy("SYSTEM");
        continent.setClientId(clientId);

        // Save the continent to the database
        continent = continentRepository.save(continent);

        logger.info("Continent with id: {} was created", continent.getId());

        // Map the saved entity to a response DTO
        return modelMapper.map(continent, ContinentResponse.class);
    }

    /**
     * Updates an existing continent.
     * @param id The ID of the continent to update.
     * @param countryRequest The continent update data from the request DTO.
     * @return The updated continent's information as a response DTO.
     */
    @Transactional
    public ContinentResponse updateContinent(UUID id, UpdateContinentRequest countryRequest) {
        logger.info("Updating Continent with id: {}", id);

        // Find the continent by ID or throw an exception if not found
        Continent country = continentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Continent Not Found"));

        // Update the continent's details
        country.setName(countryRequest.getName());
        country.setCreatedBy("SYSTEM");
        country.setModifiedBy("SYSTEM");

        logger.info("Continent updated - id: {}, name: {}", country.getId(), country.getName());

        // Save the updated continent and map it to a response DTO
        return modelMapper.map(continentRepository.save(country), ContinentResponse.class);
    }

    /**
     * Retrieves all continents in the system.
     * @return A list of continent response DTOs.
     */
    public List<ContinentResponse> getContinents() {
        logger.info("Retrieving a list of continents");

        // Retrieve all continents from the database
        List<Continent> countries = continentRepository.findAll();
        if (countries.isEmpty()) {
            logger.warn("No continents found in the database");
            return Collections.emptyList();
        } else {
            logger.info("Found {} continents", countries.size());
        }

        // Map the list of continents to response DTOs
        return countries.stream()
                .map(country -> modelMapper.map(country, ContinentResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific continent by ID.
     * @param id The ID of the continent to retrieve.
     * @return The continent's information as a response DTO.
     */
    public ContinentResponse getContinent(UUID id) {
        logger.info("Retrieving a continent with id: {}", id);

        // Find the continent by ID or throw an exception if not found
        Continent country = continentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Continent Not Found"));

        // Map the continent entity to a response DTO
        return modelMapper.map(country, ContinentResponse.class);
    }

    /**
     * Searches for continents by name, ignoring case.
     * @param name The name (or part of it) to search for.
     * @return A list of continent response DTOs matching the search criteria.
     */
    public List<ContinentResponse> getContinentByName(String name) {
        logger.info("Searching for continents containing: {}", name);

        // Search for continents by name (case-insensitive)
        List<Continent> countries = continentRepository.getByNameIgnoringCase(name);

        if (countries.isEmpty()) {
            logger.warn("No continents found with name containing: {}", name);
            return Collections.emptyList();
        }

        // Map the list of continents to response DTOs
        return countries.stream()
                .map(country -> modelMapper.map(country, ContinentResponse.class))
                .collect(Collectors.toList());
    }
}

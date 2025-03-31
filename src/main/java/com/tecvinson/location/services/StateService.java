package com.tecvinson.location.services;

import com.tecvinson.location.dtos.state.CreateStateRequest;
import com.tecvinson.location.dtos.state.StateResponse;
import com.tecvinson.location.dtos.state.UpdateStateRequest;
import com.tecvinson.location.entities.Country;
import com.tecvinson.location.entities.State;
import com.tecvinson.location.exceptions.NotFoundException;
import com.tecvinson.location.exceptions.ResourceConflictException;
import com.tecvinson.location.repositories.CountryRepository;
import com.tecvinson.location.repositories.StateRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StateService {
    private static final Logger logger = LoggerFactory.getLogger(StateService.class);

    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final ModelMapper modelMapper;

    // Constructor-based dependency injection for repositories and model mapper
    public StateService(CountryRepository countryRepository, StateRepository stateRepository, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new State and associates it with a Country.
     * @param stateRequest DTO containing the details of the state to be created.
     * @return StateResponse DTO with the created state's details.
     */
    public StateResponse createState(CreateStateRequest stateRequest) {
        logger.info("Creating a state with name: {}", stateRequest.getName());

        // Retrieve the associated Country by ID or throw an exception if not found
        Country country = countryRepository.findById(stateRequest.getCountryId())
                .orElseThrow(() -> new NotFoundException("Country not found"));

        // Check if a state with the same name already exists in the country
        boolean stateExists = stateRepository.existsByNameAndCountryId(stateRequest.getName(), stateRequest.getCountryId());
        if (stateExists) {
            throw new ResourceConflictException("State already exists for this country");
        }

        // Map the DTO to an entity and set additional properties
        State state = modelMapper.map(stateRequest, State.class);
        state.setCountry(country);
        state.setCreatedBy("SYSTEM");
        state.setModifiedBy("SYSTEM");
        state = stateRepository.save(state);

        logger.info("State with Id: {} created", state.getId());

        // Map the saved entity to a response DTO and return
        return modelMapper.map(state, StateResponse.class);
    }

    /**
     * Updates the details of an existing State.
     * @param id The UUID of the state to update.
     * @param stateRequest DTO containing the updated state details.
     * @return StateResponse DTO with updated state details.
     */
    public StateResponse updateState(UUID id, UpdateStateRequest stateRequest) {
        logger.info("Updating the details of a state with ID: {}", id);

        // Retrieve the associated Country by ID or throw an exception if not found
        Country country = countryRepository.findById(stateRequest.getCountryId())
                .orElseThrow(() -> new NotFoundException("Country not found"));

        // Check if a state with the same name already exists in the country
        boolean stateExists = stateRepository.existsByNameAndCountryId(stateRequest.getName(), stateRequest.getCountryId());
        if (stateExists) {
            throw new ResourceConflictException("State already exists for this country");
        }

        // Retrieve the state by ID or throw an exception if not found
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("State Not Found"));

        // Update the state's details
        state.setName(stateRequest.getName());
        state.setCountry(country);
        state.setCreatedBy("SYSTEM");
        state.setModifiedBy("SYSTEM");

        logger.info("State updated. id: {}, country: {}", id, country.getName());

        // Save the updated state and map it to a response DTO
        return modelMapper.map(stateRepository.save(state), StateResponse.class);
    }

    /**
     * Retrieves a list of all states.
     * @return A list of StateResponse DTOs representing all states.
     */
    public List<StateResponse> getStates() {
        logger.info("Retrieving List of all states");

        // Fetch all states from the database
        List<State> states = stateRepository.findAll();
        if (states.isEmpty()) {
            logger.warn("No states found in the database");
            return Collections.emptyList();
        } else {
            logger.info("Found {} states in the database", states.size());
        }

        // Map each State entity to a StateResponse DTO and return the list
        return states.stream()
                .map(state -> {
                    StateResponse stateResponse = modelMapper.map(state, StateResponse.class);
                    stateResponse.setCountryName(state.getCountry().getName());
                    return stateResponse;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific State by ID.
     * @param id The UUID of the state to retrieve.
     * @return StateResponse DTO with the retrieved state's details.
     */
    public StateResponse getState(UUID id) {
        logger.info("Retrieving State with id: {}", id);

        // Fetch the state by ID or throw an exception if not found
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No state Found"));

        // Map the state to a response DTO
        StateResponse stateResponse = modelMapper.map(state, StateResponse.class);
        stateResponse.setCountryName(state.getCountry().getName());
        return stateResponse;
    }

    /**
     * Searches for states by name (case-insensitive).
     * @param name The name or partial name of the states to search for.
     * @return A list of StateResponse DTOs matching the search criteria.
     */
    public List<StateResponse> getStatesByName(String name) {
        logger.info("Searching for states with name: {}", name);

        // Fetch states matching the search criteria
        List<State> states = stateRepository.getByNameIgnoringCase(name);

        if (states.isEmpty()) {
            logger.warn("No states found with name: {}", name);
            return Collections.emptyList();
        }

        // Map each State entity to a StateResponse DTO and return the list
        return states.stream()
                .map(state -> {
                    StateResponse response = modelMapper.map(state, StateResponse.class);
                    response.setCountryName(state.getCountry().getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of states for a specific country by its ID.
     * @param countryId The UUID of the country to retrieve states for.
     * @return A list of StateResponse DTOs for the specified country.
     */
    public List<StateResponse> getByCountryId(UUID countryId) {
        logger.info("Retrieving a List of states in a country with countryId: {}", countryId);

        // Fetch the country by ID or throw an exception if not found
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new NotFoundException("Country not Found"));

        // Fetch all states associated with the country
        List<State> states = stateRepository.getByCountry(country);
        if (states.isEmpty()) {
            logger.warn("No states found for country with ID: {}", countryId);
            return Collections.emptyList();
        }

        // Map each State entity to a StateResponse DTO and return the list
        return states.stream()
                .map(state -> {
                    StateResponse stateResponse = modelMapper.map(state, StateResponse.class);
                    stateResponse.setCountryName(state.getCountry().getName());
                    return stateResponse;
                })
                .collect(Collectors.toList());
    }
}

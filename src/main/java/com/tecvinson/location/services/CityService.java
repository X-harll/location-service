package com.tecvinson.location.services;

import com.tecvinson.location.dtos.city.CityResponse;
import com.tecvinson.location.dtos.city.CityRequest;
import com.tecvinson.location.entities.City;
import com.tecvinson.location.entities.Country;
import com.tecvinson.location.entities.State;
import com.tecvinson.location.exceptions.NotFoundException;
import com.tecvinson.location.exceptions.ResourceConflictException;
import com.tecvinson.location.repositories.CityRepository;
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
public class CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityService.class);

    private final ModelMapper modelMapper;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    // Constructor injection for required dependencies
    public CityService(ModelMapper modelMapper, CityRepository cityRepository,
                       StateRepository stateRepository, CountryRepository countryRepository) {
        this.modelMapper = modelMapper;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    // Method to create a new city
    public CityResponse createCity(CityRequest cityRequest) {
        logger.info("Creating a City with name {}", cityRequest.getName());

        // Fetch the associated state or throw an exception if not found
        State state = stateRepository.findById(cityRequest.getStateId())
                .orElseThrow(() -> new NotFoundException("State not Found"));

        // Check if a city with the same name already exists in the specified state
        boolean cityExists = cityRepository.existsByNameAndStateId(
                cityRequest.getName(), cityRequest.getStateId());
        if (cityExists) {
            throw new ResourceConflictException("City Exists for this State");
        }

        // Map the CityRequest DTO to a City entity
        City city = modelMapper.map(cityRequest, City.class);
        city.setState(state); // Set the associated state
        city.setCreatedBy("SYSTEM"); // Set audit fields
        city.setModifiedBy("SYSTEM");
        city = cityRepository.save(city); // Save the city to the database

        logger.info("City with id: {} created", city.getId());

        // Map the City entity to a CityResponse DTO
        CityResponse cityResponse = modelMapper.map(city, CityResponse.class);
        cityResponse.setCountryName(city.getState().getCountry().getName());
        return cityResponse;
    }

    // Method to update an existing city
    public CityResponse updateCity(UUID id, CityRequest cityRequest) {
        logger.info("Updating a city with id {}", id);

        // Fetch the associated state or throw an exception if not found
        State state = stateRepository.findById(cityRequest.getStateId())
                .orElseThrow(() -> new NotFoundException("State not Found"));

        // Check if a city with the same name already exists in the specified state
        boolean cityExists = cityRepository.existsByNameAndStateId(
                cityRequest.getName(), cityRequest.getStateId());
        if (cityExists) {
            throw new ResourceConflictException("City Exists for this State");
        }

        // Fetch the existing city or throw an exception if not found
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City not Found"));

        // Update the city entity fields
        city.setName(cityRequest.getName());
        city.setState(state);
        city.setCreatedBy("SYSTEM");
        city.setModifiedBy("SYSTEM");

        logger.info("City Updated. Id:{}, State:{}", id, state.getName());

        // Save the updated city and map to CityResponse DTO
        CityResponse cityResponse = modelMapper.map(cityRepository.save(city), CityResponse.class);
        cityResponse.setCountryName(city.getState().getCountry().getName());
        return cityResponse;
    }

    // Method to retrieve all cities
    public List<CityResponse> getCities() {
        logger.info("Retrieving All Cities");

        // Fetch all cities from the repository
        List<City> cities = cityRepository.findAll();
        if (cities.isEmpty()) {
            logger.warn("No City found in Database");
            return Collections.emptyList();
        } else {
            logger.info("Found {} cities in the Database", cities.size());
        }

        // Map each City entity to a CityResponse DTO
        return cities.stream().map(city -> {
            CityResponse cityResponse = modelMapper.map(city, CityResponse.class);
            cityResponse.setStateName(city.getState().getName());
            cityResponse.setCountryName(city.getState().getCountry().getName());
            return cityResponse;
        }).collect(Collectors.toList());
    }

    // Method to retrieve a city by its ID
    public CityResponse getCity(UUID id) {
        logger.info("Retrieving a City with {} id", id);

        // Fetch the city by its ID or throw an exception if not found
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("City not found"));

        // Map the City entity to a CityResponse DTO
        CityResponse cityResponse = modelMapper.map(city, CityResponse.class);
        cityResponse.setStateName(city.getState().getName());
        cityResponse.setCountryName(city.getState().getCountry().getName());

        return cityResponse;
    }

    // Method to retrieve cities by their name
    public List<CityResponse> getByName(String name) {
        logger.info("Retrieving Cities By Name {}", name);

        // Fetch cities by name using a custom query
        List<City> cities = cityRepository.getByNameIgnoringCase(name);
        if (cities.isEmpty()) {
            logger.warn("No City with such name found in Database");
            return Collections.emptyList();
        } else {
            logger.info("Found {} cities with name {} in the Database", cities.size(), name);
        }

        // Map each City entity to a CityResponse DTO
        return cities.stream().map(city -> {
            CityResponse cityResponse = modelMapper.map(city, CityResponse.class);
            cityResponse.setStateName(city.getState().getName());
            cityResponse.setCountryName(city.getState().getCountry().getName());
            return cityResponse;
        }).collect(Collectors.toList());
    }

    // Method to retrieve cities by their state ID
    public List<CityResponse> getByState(UUID stateId) {
        logger.info("Retrieving Cities in state: {}", stateId);

        // Fetch the state by its ID or throw an exception if not found
        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new NotFoundException("State not Found"));

        // Fetch cities in the state
        List<City> cities = cityRepository.getByState(state);
        if (cities.isEmpty()) {
            logger.warn("No cities found in the state: {}", state.getName());
            return Collections.emptyList();
        } else {
            logger.info("Found {} cities in state {} in the Database", cities.size(), state);
        }

        // Map each City entity to a CityResponse DTO
        return cities.stream().map(city -> {
            CityResponse cityResponse = modelMapper.map(city, CityResponse.class);
            cityResponse.setStateName(city.getState().getName());
            cityResponse.setCountryName(city.getState().getCountry().getName());
            return cityResponse;
        }).collect(Collectors.toList());
    }

    // Method to retrieve cities by their country ID
    public List<CityResponse> getByCountry(UUID countryId) {
        logger.info("Retrieving Cities in Country with ID: {}", countryId);

        // Fetch the country by its ID or throw an exception if not found
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new NotFoundException("Country not Found"));

        // Fetch all cities from the repository
        List<City> allCities = cityRepository.findAll();
        if (allCities.isEmpty()) {
            logger.warn("No Cities found");
            return Collections.emptyList();
        }

        // Filter cities that belong to the given country
        List<City> filteredCities = allCities.stream()
                .filter(city -> city.getState().getCountry().getId().equals(countryId))
                .toList();

        // Map the filtered cities to CityResponse DTOs
        return filteredCities.stream()
                .map(city -> {
                    CityResponse cityResponse = modelMapper.map(city, CityResponse.class);
                    cityResponse.setStateName(city.getState().getName());
                    cityResponse.setCountryName(city.getState().getCountry().getName());
                    return cityResponse;
                })
                .collect(Collectors.toList());
    }
}

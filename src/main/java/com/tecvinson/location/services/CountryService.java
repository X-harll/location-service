package com.tecvinson.location.services;

import com.tecvinson.location.dtos.country.CountryResponse;
import com.tecvinson.location.dtos.country.CountryRequest;
import com.tecvinson.location.entities.Continent;
import com.tecvinson.location.entities.Country;
import com.tecvinson.location.exceptions.NotFoundException;
import com.tecvinson.location.repositories.ContinentRepository;
import com.tecvinson.location.repositories.CountryRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;
    private final ContinentRepository continentRepository;
    private final ModelMapper modelMapper;

    // Constructor-based dependency injection for repositories and model mapper
    public CountryService(CountryRepository countryRepository, ContinentRepository continentRepository, ModelMapper modelMapper) {
        this.countryRepository = countryRepository;
        this.continentRepository = continentRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new Country entity and saves it to the database.
     * @param countryRequest DTO containing details of the country to be created.
     * @return CountryResponse DTO with the details of the created country.
     */
    @Transactional
    public CountryResponse createCountry(CountryRequest countryRequest) {
        logger.info("Creating a Country with name: {}", countryRequest.getName());

        // Retrieve the associated continent by ID or throw an exception if not found
        Continent continent = continentRepository.findById(countryRequest.getContinentId())
                .orElseThrow(() -> new NotFoundException("Continent Not found"));


            // Map the DTO to an entity and set additional properties
            Country country = modelMapper.map(countryRequest, Country.class);
            country.setCreatedBy("SYSTEM");
            country.setModifiedBy("SYSTEM");
            country.setContinent(continent);

            // Save the new country to the database
            country = countryRepository.save(country);

            logger.info("Country with id: {} was created", country.getId());

            // Map the saved entity to a response DTO and return
            return modelMapper.map(country, CountryResponse.class);

    }

    /**
     * Updates an existing Country entity.
     * @param id The UUID of the country to update.
     * @param countryRequest DTO containing updated country details.
     * @return CountryResponse DTO with updated details of the country.
     */
    @Transactional
    public CountryResponse updateCountry(UUID id, CountryRequest countryRequest) {
        logger.info("Updating Country with id: {}", id);

        // Retrieve the associated continent and country entities by ID
        Continent continent = continentRepository.findById(countryRequest.getContinentId())
                .orElseThrow(() -> new NotFoundException("Continent Not found"));

        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Country not Found"));


            // Update entity properties with the new data
            country.setName(countryRequest.getName());
            country.setCountryCode(countryRequest.getCountryCode());
            country.setPhoneCode(countryRequest.getPhoneCode());
            country.setFlag(countryRequest.getFlag());
            country.setContinent(continent);
            country.setCreatedBy("SYSTEM");
            country.setModifiedBy("SYSTEM");

            logger.info("Country updated - id: {}, name: {}", country.getId(), country.getName());

            // Save the updated country to the database and map it to a response DTO
            return modelMapper.map(countryRepository.save(country), CountryResponse.class);
    }

    /**
     * Retrieves a list of all countries.
     * @return A list of CountryResponse DTOs representing all countries.
     */
    public List<CountryResponse> getCountries() {
        logger.info("Retrieving a list of countries");

        // Fetch all countries from the database
        List<Country> countries = countryRepository.findAll();
        if (countries.isEmpty()) {
            logger.warn("No countries found in the database");
            return Collections.emptyList();
        } else {
            logger.info("Found {} countries", countries.size());
        }

        // Map each Country entity to a CountryResponse DTO and return the list
        return countries.stream()
                .map(country -> {
                    CountryResponse countryResponse = modelMapper.map(country, CountryResponse.class);
                    countryResponse.setContinentName(country.getContinent().getName());
                    return countryResponse;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves details of a specific country by ID.
     * @param id The UUID of the country to retrieve.
     * @return CountryResponse DTO with details of the retrieved country.
     */
    public CountryResponse getCountry(UUID id) {
        logger.info("Retrieving a country with id: {}", id);

        // Fetch the country by ID or throw an exception if not found
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Country not found"));

        // Map the entity to a response DTO and return
        return modelMapper.map(country, CountryResponse.class);
    }

    /**
     * Searches for countries with names containing the specified string (case-insensitive).
     * @param name The string to search for in country names.
     * @return A list of CountryResponse DTOs matching the search criteria.
     */
    public List<CountryResponse> getCountriesByName(String name) {
        logger.info("Searching for countries containing: {}", name);

        // Fetch countries matching the search criteria
        List<Country> countries = countryRepository.getByNameIgnoringCase(name);

        if (countries.isEmpty()) {
            logger.warn("No countries found with name containing: {}", name);
            return Collections.emptyList();
        }

        // Map each Country entity to a CountryResponse DTO and return the list
        return countries.stream()
                .map(country -> {
                    CountryResponse countryResponse = modelMapper.map(country, CountryResponse.class);
                    countryResponse.setContinentName(country.getContinent().getName());
                    return countryResponse;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of states for a specific country by its ID.
     * @param continentId The UUID of the country to retrieve states for.
     * @return A list of StateResponse DTOs for the specified country.
     */
    public List<CountryResponse> getByContinent(UUID continentId) {
        logger.info("Retrieving a list of Countries in a Continent with Id: {}",continentId);

        // Fetch the continent by ID or throw an exception if not found
        Continent continent = continentRepository.findById(continentId)
                .orElseThrow(() -> new NotFoundException("Continent not found"));

        //Fetch all Countries associated with the Continent
        List<Country> countries = countryRepository.getByContinent(continent);
        if (countries.isEmpty()) {
            logger.warn("No Country found for Continent with Id:{}",continentId);
            return Collections.emptyList();
        }

        // Map each Country entity to a CountryResponse DTO and return the list
        return countries.stream()
                .map(country -> {
                    CountryResponse countryResponse = modelMapper.map(country, CountryResponse.class );
                    countryResponse.setContinentName(country.getContinent().getName());
                    return countryResponse;
                })
                .collect(Collectors.toList());
    }

}

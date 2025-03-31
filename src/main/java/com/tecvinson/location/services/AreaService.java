package com.tecvinson.location.services;

import com.tecvinson.location.dtos.area.AreaResponse;
import com.tecvinson.location.dtos.area.CreateAreaRequest;
import com.tecvinson.location.dtos.area.UpdateAreaRequest;
import com.tecvinson.location.entities.Area;
import com.tecvinson.location.entities.City;
import com.tecvinson.location.entities.Country;
import com.tecvinson.location.entities.State;
import com.tecvinson.location.exceptions.NotFoundException;
import com.tecvinson.location.exceptions.ResourceConflictException;
import com.tecvinson.location.repositories.AreaRepository;
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
public class AreaService {

    // Logger to track activities in the service
    private static final Logger logger = LoggerFactory.getLogger(AreaService.class);

    // Repositories for interacting with the database
    private final ModelMapper modelMapper;
    private final AreaRepository areaRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;

    // Constructor to inject dependencies
    public AreaService(ModelMapper modelMapper, AreaRepository areaRepository, CityRepository cityRepository, StateRepository stateRepository, CountryRepository countryRepository) {
        this.modelMapper = modelMapper;
        this.areaRepository = areaRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    // Method to create a new Area
    public AreaResponse createArea(CreateAreaRequest areaRequest) {
        logger.info("Creating Area with name {}", areaRequest.getName());

        // Validate that the City exists
        City city = cityRepository.findById(areaRequest.getCityId())
                .orElseThrow(() -> new NotFoundException("City not Found"));

        // Check if an Area with the same name already exists in the City
        boolean areaExists = areaRepository.existsByNameAndCityId(
                areaRequest.getName(), areaRequest.getCityId());
        if (areaExists) {
            throw new ResourceConflictException("Area Exists for this City");
        }

        // Map the request to an Area entity and set the City
        Area area = modelMapper.map(areaRequest, Area.class);
        area.setCity(city);
        area.setCreatedBy("SYSTEM");
        area.setModifiedBy("SYSTEM");

        // Save the Area entity to the database
        area = areaRepository.save(area);

        logger.info("Area with id: {} created", area.getId());

        // Map the Area entity to a response DTO and set additional fields
        AreaResponse areaResponse = modelMapper.map(area, AreaResponse.class);
        areaResponse.setStateName(area.getCity().getState().getName());
        areaResponse.setCountryName(area.getCity().getState().getCountry().getName());

        return areaResponse;
    }

    // Method to update an existing Area
    public AreaResponse updateArea(UUID id, UpdateAreaRequest areaRequest) {
        logger.info("Updating area with id {}", id);

        // Validate that the City exists
        City city = cityRepository.findById(areaRequest.getCityId())
                .orElseThrow(() -> new NotFoundException("City not Found"));

        // Check if the Area with the same name already exists in the City
        boolean areaExists = areaRepository.existsByNameAndCityId(
                areaRequest.getName(), areaRequest.getCityId());
        if (areaExists) {
            throw new ResourceConflictException("Area Exists for this City");
        }

        // Retrieve the existing Area from the database
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Area not Found"));

        // Update the Area details
        area.setName(areaRequest.getName());
        area.setCity(city);
        area.setCreatedBy("SYSTEM");
        area.setModifiedBy("SYSTEM");

        logger.info("Area Updated. Id:{}, City:{}",
                id, city.getName());

        // Save the updated Area entity and map to response DTO
        AreaResponse areaResponse = modelMapper.map(areaRepository.save(area), AreaResponse.class);
        areaResponse.setStateName(area.getCity().getState().getName());
        areaResponse.setCountryName(area.getCity().getState().getCountry().getName());

        return areaResponse;
    }

    // Method to retrieve all Areas
    public List<AreaResponse> getAreas() {
        logger.info("Retrieving All Areas");

        // Fetch all areas from the repository
        List<Area> areas = areaRepository.findAll();
        if (areas.isEmpty()) {
            logger.warn("No Areas found in Database");
            return Collections.emptyList();
        } else {
            logger.info("Found {} areas in the Database", areas.size());
        }

        // Convert the Areas to AreaResponse DTOs and include city, state, and country information
        return areas.stream().map(area -> {
                    AreaResponse areaResponse = modelMapper.map(area, AreaResponse.class);
                    areaResponse.setCityName(area.getCity().getName());
                    areaResponse.setStateName(area.getCity().getState().getName());
                    areaResponse.setCountryName(area.getCity().getState().getCountry().getName());
                    return areaResponse;
                })
                .collect(Collectors.toList());
    }

    // Method to retrieve a specific Area by its ID
    public AreaResponse getArea(UUID id) {
        logger.info("Retrieving an Area with {} id", id);

        // Fetch the Area from the repository
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Area not found"));

        // Convert the Area to AreaResponse DTO and include city, state, and country information
        AreaResponse areaResponse = modelMapper.map(area, AreaResponse.class);
        areaResponse.setCityName(area.getCity().getName());
        areaResponse.setStateName(area.getCity().getState().getName());
        areaResponse.setCountryName(area.getCity().getState().getCountry().getName());

        return areaResponse;
    }

    // Method to retrieve Areas by Name (case-insensitive)
    public List<AreaResponse> getByName(String name) {
        logger.info("Retrieving Areas By Name {}", name);

        // Fetch Areas that match the name from the repository (ignoring case)
        List<Area> areas = areaRepository.getByNameIgnoringCase(name);
        if (areas.isEmpty()) {
            logger.warn("No Area with name containing '{}' found in the database", name);
            return Collections.emptyList();
        } else {
            logger.info("Found {} area(s) containing '{}' in the database", areas.size(), name);
        }

        // Convert the Areas to AreaResponse DTOs and include city, state, and country information
        return areas.stream().map(area -> {
                    AreaResponse areaResponse = modelMapper.map(area, AreaResponse.class);
                    areaResponse.setCityName(area.getCity().getName());
                    areaResponse.setStateName(area.getCity().getState().getName());
                    areaResponse.setCountryName(area.getCity().getState().getCountry().getName());
                    return areaResponse;
                })
                .collect(Collectors.toList());
    }

    // Method to retrieve Areas within a specific City
    public List<AreaResponse> getByCity(UUID cityId) {
        logger.info("Retrieving Areas in City: {}", cityId);

        // Validate that the City exists
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("City not Found"));

        // Fetch Areas for the specified City
        List<Area> areas = areaRepository.getByCity(city);
        if (areas.isEmpty()) {
            logger.warn("No areas found in the city: {}", city.getName());
            return Collections.emptyList();
        } else {
            logger.info("Found {} areas in city '{}' in the Database", areas.size(), city.getName());
        }

        // Convert the Areas to AreaResponse DTOs and include city, state, and country information
        return areas.stream().map(area -> {
                    AreaResponse areaResponse = modelMapper.map(area, AreaResponse.class);
                    areaResponse.setCityName(area.getCity().getName());
                    areaResponse.setStateName(area.getCity().getState().getName());
                    areaResponse.setCountryName(area.getCity().getState().getCountry().getName());
                    return areaResponse;
                })
                .collect(Collectors.toList());
    }

    // Method to retrieve Areas within a specific State
    public List<AreaResponse> getByState(UUID stateId) {
        logger.info("Retrieving Areas in State: {}", stateId);

        // Validate that the State exists
        State state = stateRepository.findById(stateId)
                .orElseThrow(() -> new NotFoundException("State not Found"));

        // Fetch all Areas and filter them by State
        List<Area> allAreas = areaRepository.findAll();
        if (allAreas.isEmpty()) {
            logger.warn("No areas found in the state: {}", state.getName());
            return Collections.emptyList();
        }

        List<Area> filteredAreas = allAreas.stream().filter(area ->
                        area.getCity().getState().getId().equals(stateId))
                .toList();

        // Convert the Areas to AreaResponse DTOs and include city, state, and country information
        return filteredAreas.stream().map(area -> {
                    AreaResponse areaResponse = modelMapper.map(area, AreaResponse.class);
                    areaResponse.setCityName(area.getCity().getName());
                    areaResponse.setStateName(area.getCity().getState().getName());
                    areaResponse.setCountryName(area.getCity().getState().getCountry().getName());
                    return areaResponse;
                })
                .collect(Collectors.toList());
    }

    // Method to retrieve Areas within a specific Country
    public List<AreaResponse> getByCountry(UUID countryId) {
        logger.info("Retrieving Areas in Country: {}", countryId);

        // Validate that the Country exists
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new NotFoundException("Country not Found"));

        // Fetch all Areas and filter them by Country
        List<Area> allAreas = areaRepository.findAll();
        if (allAreas.isEmpty()) {
            logger.warn("No areas found in the country: {}", country.getName());
            return Collections.emptyList();
        }

        List<Area> filteredAreas = allAreas.stream().filter(
                        area -> area.getCity().getState().getCountry().getId().equals(countryId))
                .toList();

        // Convert the Areas to AreaResponse DTOs and include city, state, and country information
        return filteredAreas.stream().map(area -> {
                    AreaResponse areaResponse = modelMapper.map(area, AreaResponse.class);
                    areaResponse.setCityName(area.getCity().getName());
                    areaResponse.setStateName(area.getCity().getState().getName());
                    areaResponse.setCountryName(area.getCity().getState().getCountry().getName());
                    return areaResponse;
                })
                .collect(Collectors.toList());
    }
}

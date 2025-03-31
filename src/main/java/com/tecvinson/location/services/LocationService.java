package com.tecvinson.location.services;

import com.tecvinson.location.dtos.location.CreateLocationRequest;
import com.tecvinson.location.dtos.location.LocationResponse;
import com.tecvinson.location.dtos.location.UpdateLocationRequest;
import com.tecvinson.location.entities.*;
import com.tecvinson.location.exceptions.NotFoundException;
import com.tecvinson.location.repositories.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class LocationService {

    public static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final ModelMapper modelMapper;
    private final LocationRepository locationRepository;
    private final AreaRepository areaRepository;
    private  final CityRepository cityRepository;
    private  final StateRepository stateRepository;
    private  final CountryRepository countryRepository;

    public LocationService(ModelMapper modelMapper, LocationRepository locationRepository, AreaRepository areaRepository, CityRepository cityRepository, StateRepository stateRepository, CountryRepository countryRepository) {
        this.modelMapper = modelMapper;
        this.locationRepository = locationRepository;
        this.areaRepository = areaRepository;
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }


    public LocationResponse createLocation(CreateLocationRequest locationRequest) {
        logger.info("Creating a Location");

        Area area = areaRepository.findById(locationRequest.getAreaId())
                .orElseThrow(() -> new NotFoundException("Area not Found"));

        Location location = modelMapper.map(locationRequest, Location.class);
        location.setArea(area);
        location.setCreatedBy("SYSTEM");
        location.setModifiedBy("SYSTEM");
        location = locationRepository.save(location);

        logger.info("Location with Id {} created", location.getId());

        LocationResponse locationResponse = modelMapper.map(location, LocationResponse.class);
        locationResponse.setCityName(location.getArea().getCity().getName());
        locationResponse.setStateName(location.getArea().getCity().getState().getName());
        locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
        return locationResponse;
    }


    public LocationResponse updateLocation(UUID id, UpdateLocationRequest locationRequest) {
        logger.info("Updating Location with Id {}", id);

        Area area = areaRepository.findById(locationRequest.getAreaId())
                .orElseThrow(() -> new NotFoundException("Area not Found"));

        Location location = locationRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Location not Found"));

        location.setHouseAddress(locationRequest.getHouseAddress());
        location.setStreetName(location.getStreetName());
        location.setArea(area);
        location.setCreatedBy("SYSTEM");
        location.setModifiedBy("SYSTEM");
        location.setLatitude(locationRequest.getLatitude());
        location.setLongitude(locationRequest.getLongitude());

        LocationResponse locationResponse = modelMapper.map(locationRepository.save(location), LocationResponse.class);
        locationResponse.setCityName(location.getArea().getCity().getName());
        locationResponse.setStateName(location.getArea().getCity().getState().getName());
        locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
        return locationResponse;
    }

    public List<LocationResponse> getLocations() {
        logger.info("Retrieving all locations");

        List<Location> locations = locationRepository.findAll();
        if (locations.isEmpty()) {
            logger.warn("No location found in the database");
            return Collections.emptyList();
        } else {
            logger.info("Found {} Locations in the database", locations.size());


            return locations.stream()
                    .map(location -> {
                        LocationResponse locationResponse = modelMapper.map(location, LocationResponse.class);
                        locationResponse.setAreaName(location.getArea().getName());
                        locationResponse.setCityName(location.getArea().getCity().getName());
                        locationResponse.setStateName(location.getArea().getCity().getState().getName());
                        locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
                        return locationResponse;
                    })
                    .collect(Collectors.toList());
        }
    }

    public LocationResponse getLocation(UUID id) {
        logger.info("Retrieving a location with Id: {}",id);

        Location location = locationRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Location Not found"));

        LocationResponse locationResponse = modelMapper.map(location, LocationResponse.class);
        locationResponse.setAreaName(location.getArea().getName());
        locationResponse.setCityName(location.getArea().getCity().getName());
        locationResponse.setStateName(location.getArea().getCity().getState().getName());
        locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
        return locationResponse;
    }


    public List<LocationResponse> getLocationByCountry(UUID countryId) {
        logger.info("Retrieving All Locations in a Country with Id:{}",countryId);

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new NotFoundException("Country Not Found"));

        List <Location> allLocations = locationRepository.findAll();
        if (allLocations.isEmpty()) {
            logger.warn("No Country Found in The Database");
            return Collections.emptyList();
        }

        List<Location> filteredLocations = allLocations.stream().filter(
                location -> location.getArea().getCity().getState().getCountry().getId().equals(countryId))
                .toList();


        return filteredLocations.stream()
                .map(location -> {
                    LocationResponse locationResponse = modelMapper.map(location, LocationResponse.class);
                    locationResponse.setAreaName(location.getArea().getName());
                    locationResponse.setCityName(location.getArea().getCity().getName());
                    locationResponse.setStateName(location.getArea().getCity().getState().getName());
                    locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
                    return locationResponse;
                })
                .collect(Collectors.toList());
    }

    public List<LocationResponse> getLocationByState(UUID stateId) {
        logger.info("Retrieving All Locations in a State with Id: {}", stateId);

        State states = stateRepository.findById(stateId)
                .orElseThrow(()-> new NotFoundException("State Not Found"));

        List <Location> allLocations = locationRepository.findAll();
        if (allLocations.isEmpty()){
            logger.warn("No Location Found in The Database");
            return Collections.emptyList();
        }

        List<Location> filteredLocations = allLocations.stream().filter(
                location -> location.getArea().getCity().getState().getId().equals(stateId))
                .toList();

        return filteredLocations.stream()
                .map(location -> {
                    LocationResponse locationResponse = modelMapper.map(location, LocationResponse.class);
                    locationResponse.setAreaName(location.getArea().getName());
                    locationResponse.setCityName(location.getArea().getCity().getName());
                    locationResponse.setStateName(location.getArea().getCity().getState().getName());
                    locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
                    return locationResponse;
                })
                .collect(Collectors.toList());
    }

    public List<LocationResponse> getLocationByCity(UUID cityId) {
        logger.info("Retrieving All Locations in a City with Id: {}", cityId);

        City cities = cityRepository.findById(cityId)
                .orElseThrow(()-> new NotFoundException("No city Found"));

        List<Location> allLocation = locationRepository.findAll();
        if (allLocation.isEmpty()){
            logger.warn("No Location Found in The Database");
            return Collections.emptyList();
        }

        List<Location> filteredLocations = allLocation.stream().filter(
                location -> location.getArea().getCity().getId().equals(cityId))
                .toList();

        return filteredLocations.stream()
                .map(location -> {
                    LocationResponse locationResponse = modelMapper.map(location, LocationResponse.class);
                    locationResponse.setAreaName(location.getArea().getName());
                    locationResponse.setCityName(location.getArea().getCity().getName());
                    locationResponse.setStateName(location.getArea().getCity().getState().getName());
                    locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
                    return locationResponse;
                })
                .collect(Collectors.toList());
    }

    public List<LocationResponse> getLocationByArea(UUID areaId) {
        logger.info("Retrieving All Locations in an Area with Id: {},", areaId);

        List<Area> areas = areaRepository.findAll();
        if (areas.isEmpty()){
            logger.warn("No Area Found in The Database");
            return Collections.emptyList();
        }

        List<Location> allLocation = locationRepository.findAll();
        if (allLocation.isEmpty()){
            logger.warn("No Location Found in The Database");
            return Collections.emptyList();
        }

        List<Location> filteredLocations = allLocation.stream().filter(
                        location -> location.getArea().getId().equals(areaId))
                .toList();

        return filteredLocations.stream()
                .map(location -> {
                    LocationResponse locationResponse = modelMapper.map(location, LocationResponse.class);
                    locationResponse.setAreaName(location.getArea().getName());
                    locationResponse.setCityName(location.getArea().getCity().getName());
                    locationResponse.setStateName(location.getArea().getCity().getState().getName());
                    locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
                    return locationResponse;
                })
                .collect(Collectors.toList());
    }

    public List<LocationResponse> getBySearch(String searchTerm) {
        logger.info("Retrieving Locations matching search term: {}", searchTerm);

        List<Location> locations = locationRepository.findByHouseAddressOrStreetName(searchTerm);
        if (locations.isEmpty()) {
            logger.warn("No Locations found for '{}'", searchTerm);
            return Collections.emptyList();
        }

        logger.info("Found {} location(s) for '{}'", locations.size(), searchTerm);

        return locations.stream()
                .map(location -> {
                    LocationResponse locationResponse = modelMapper.map(location, LocationResponse.class);
                    locationResponse.setAreaName(location.getArea().getName());
                    locationResponse.setCityName(location.getArea().getCity().getName());
                    locationResponse.setStateName(location.getArea().getCity().getState().getName());
                    locationResponse.setCountryName(location.getArea().getCity().getState().getCountry().getName());
                    return locationResponse;
                })
                .collect(Collectors.toList());
    }



}

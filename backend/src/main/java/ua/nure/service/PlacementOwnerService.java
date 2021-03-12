package ua.nure.service;

import ua.nure.dto.PlacementOwnerDto;
import ua.nure.dto.PlacementDto;
import ua.nure.dto.SmartDeviceDto;

import java.util.List;
import java.util.Set;

public interface PlacementOwnerService {

    PlacementOwnerDto create(PlacementOwnerDto placementOwnerDto);

    PlacementOwnerDto update(PlacementOwnerDto placementOwnerDto);

    void delete(PlacementOwnerDto placementOwnerDto);

    List<PlacementOwnerDto> findAll();

    PlacementOwnerDto findByPhoneNumber(String phoneNumber);

    PlacementOwnerDto findByEmail(String email);

    PlacementOwnerDto findById(Long id);

    Set<PlacementDto> findAllPlacements(String email);

    PlacementDto addPlacement(PlacementDto placementDto, String email);

    PlacementDto updatePlacement(PlacementDto placementDto, String email);

    PlacementDto findPlacementById(Long id);

    void deletePlacement(PlacementDto placementDto);

    PlacementDto updateSmartDevice(SmartDeviceDto smartDeviceDto);
}

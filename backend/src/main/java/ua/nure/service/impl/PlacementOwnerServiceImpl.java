package ua.nure.service.impl;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nure.dto.PlacementOwnerDto;
import ua.nure.dto.PlacementDto;
import ua.nure.dto.SmartDeviceDto;
import ua.nure.dto.mapper.AddressMapper;
import ua.nure.dto.mapper.PlacementOwnerMapper;
import ua.nure.dto.mapper.PlacementMapper;
import ua.nure.entity.owner.PlacementOwner;
import ua.nure.entity.owner.Placement;
import ua.nure.entity.user.SmartDevice;
import ua.nure.entity.role.UserRole;
import ua.nure.repository.PlacementOwnerRepository;
import ua.nure.repository.PlacementRepository;
import ua.nure.service.ContractService;
import ua.nure.service.PlacementOwnerService;
import ua.nure.service.RoleService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.apache.commons.lang3.time.DateUtils.isSameDay;
import static ua.nure.dto.mapper.PlacementOwnerMapper.toPlacementOwnerDto;

@Service
public class PlacementOwnerServiceImpl implements PlacementOwnerService {

    private final PlacementOwnerRepository placementOwnerRepository;

    private final PlacementRepository placementRepository;

    private final RoleService roleService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ContractService contractService;

    @Autowired
    public PlacementOwnerServiceImpl(
            PlacementOwnerRepository placementOwnerRepository,
            PlacementRepository placementRepository, RoleService roleService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            ContractService contractService) {
        this.placementOwnerRepository = placementOwnerRepository;
        this.placementRepository = placementRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.contractService = contractService;
    }

    @Override
    public PlacementOwnerDto create(PlacementOwnerDto placementOwnerDto) {
        PlacementOwner placementOwner = toPlacementOwner(placementOwnerDto,
                new PlacementOwner());

        return toPlacementOwnerDto(
                placementOwnerRepository.save(placementOwner));
    }

    @Override
    public PlacementOwnerDto update(PlacementOwnerDto placementOwnerDto) {
        Optional<PlacementOwner> placementOwner = placementOwnerRepository
                .findByEmail(placementOwnerDto.getEmail());

        if (placementOwner.isPresent()) {
            PlacementOwner placementOwnerModel = 
                    toPlacementOwner(placementOwnerDto, placementOwner.get());

            return toPlacementOwnerDto(
                    placementOwnerRepository.save(placementOwnerModel)
            );
        }

        return null;
    }

    @Override
    public void delete(PlacementOwnerDto placementOwnerDto) {
        placementOwnerRepository.deleteById(placementOwnerDto.getId());
    }

    @Override
    public List<PlacementOwnerDto> findAll() {
        List<PlacementOwner> placementOwners = 
                placementOwnerRepository.findAll();
        List<PlacementOwnerDto> placementOwnersDto = new ArrayList<>();
        
        placementOwners.forEach(placementOwner ->  
                placementOwnersDto.add(toPlacementOwnerDto(placementOwner))
        );
        
        return placementOwnersDto;
    }

    @Override
    public PlacementOwnerDto findByPhoneNumber(String phoneNumber) {
        Optional<PlacementOwner> placementOwner = placementOwnerRepository
                .findByPhoneNumber(phoneNumber);
        return placementOwner.map(PlacementOwnerMapper::toPlacementOwnerDto)
                .orElse(null);
    }

    @Override
    public PlacementOwnerDto findByEmail(String email) {
        Optional<PlacementOwner> placementOwner = placementOwnerRepository
                .findByEmail(email);
        return placementOwner.map(PlacementOwnerMapper::toPlacementOwnerDto)
                .orElse(null);
    }

    @Override
    public PlacementOwnerDto findById(Long id) {
        Optional<PlacementOwner> placementOwner = placementOwnerRepository
                .findById(id);
        return placementOwner.map(PlacementOwnerMapper::toPlacementOwnerDto)
                .orElse(null);
    }

    @Override
    public Set<PlacementDto> findAllPlacements(String email) {
        Optional<PlacementOwner> owner = placementOwnerRepository
                .findByEmail(email);
        if (owner.isPresent()) {
            Set<Placement> placements = owner.get().getPlacements();
            Set<PlacementDto> roomsDto = new HashSet<>();

            placements.forEach(placement ->
                    roomsDto.add(PlacementMapper.toPlacementDto(placement))
            );

            return roomsDto;
        }
        return Collections.emptySet();
    }

    @Override
    public PlacementDto addPlacement(PlacementDto placementDto, String email) {
        return savePlacement(placementDto,
                placementOwnerRepository.findByEmail(email));
    }

    @Override
    public PlacementDto updatePlacement(PlacementDto placementDto,
            String email) {
        Optional<PlacementOwner> owner =
                placementOwnerRepository.findByEmail(email);

        if (owner.isPresent()) {
            Optional<Placement> placement =
                    placementRepository.findById(placementDto.getId());

            if (placement.isPresent()) {
                return savePlacement(placementDto, owner);
            }
        }

        return null;
    }

    @Override
    public void deletePlacement(PlacementDto placementDto) {
        placementRepository.deleteById(placementDto.getId());
    }

    @Override
    public PlacementDto findPlacementById(Long id) {
        return placementRepository.findById(id)
                .map(PlacementMapper::toPlacementDto).orElse(null);
    }

    @Override
    public PlacementDto updateSmartDevice(SmartDeviceDto smartDeviceDto) {
        Optional<Placement> placementDto = placementRepository
                .findById(smartDeviceDto.getId());

        if (placementDto.isPresent()) {
            Placement placement = placementDto.get();

            SmartDevice smartDevice = placement.getSmartDevice();
            double previousAdjustmentFactor =
                    smartDevice.getAdjustmentFactor();
            double adjustmentFactor =
                    round(smartDeviceDto.getAdjustmentFactor());
            double airQuality = round(smartDeviceDto.getAirQuality());

            Date currentDate = new Date();
            placement.getContracts().stream()
                    .filter(contract -> contract.getDate().after(currentDate))
                    .forEach(contract -> {
                        double price = contract.getPrice();

                        if (previousAdjustmentFactor != 0) {
                            price /= previousAdjustmentFactor;
                        }

                        if (adjustmentFactor != 0) {
                            price *= adjustmentFactor;
                        }

                        contract.setPrice(round(price));
                    });

            double dirtinessFactor = round((1 - airQuality) + (adjustmentFactor - 1));

            smartDevice
                    .setAirQuality(airQuality)
                    .setTemperature(smartDeviceDto.getTemperature())
                    .setHumidity(smartDeviceDto.getHumidity())
                    .setAdjustmentFactor(adjustmentFactor)
                    .setDirtinessFactor(dirtinessFactor)
                    .setPriority(smartDeviceDto.getPriority());
            placement.setSmartDevice(smartDevice);
            System.out.println(smartDevice);
            return PlacementMapper
                    .toPlacementDto(placementRepository.save(placement));
        }

        return null;
    }

    private PlacementDto savePlacement(PlacementDto placementDto,
            Optional<PlacementOwner> owner) {
        Placement placement = PlacementMapper.toPlacement(placementDto);
        SmartDevice smartDevice = placement.getSmartDevice();

        if (smartDevice == null) {
            smartDevice = new SmartDevice();
        }
        smartDevice.setPlacement(placement);
        smartDevice.setId(placement.getId());
        setDeviceIndicators(smartDevice);
        placement.setSmartDevice(smartDevice);

        owner.ifPresent(placement::setPlacementOwner);

        return PlacementMapper
                .toPlacementDto(placementRepository.save(placement));
    }

    private void setDeviceIndicators(SmartDevice smartDevice) {
        if (smartDevice.getAdjustmentFactor() == null) {
            smartDevice.setAdjustmentFactor(0.0);
        }

        if (smartDevice.getAirQuality() == null) {
            smartDevice.setAirQuality(0.0);
        }

        if (smartDevice.getHumidity() == null) {
            smartDevice.setHumidity(0.0);
        }

        if (smartDevice.getDirtinessFactor() == null) {
            smartDevice.setDirtinessFactor(0.0);
        }

        if (smartDevice.getTemperature() == null) {
            smartDevice.setTemperature(0.0);
        }

        if (smartDevice.getPriority() == null) {
            smartDevice.setPriority("LOW");
        }
    }

    private double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private PlacementOwner toPlacementOwner(PlacementOwnerDto placementOwnerDto,
            PlacementOwner placementOwner) {
        placementOwner.setEmail(placementOwnerDto.getEmail());
        placementOwner.setPhoneNumber(placementOwnerDto.getPhoneNumber());
        placementOwner.setName(placementOwnerDto.getName());
        placementOwner.setCreationDate(placementOwnerDto.getCreationDate());
        placementOwner.setAddress(
                AddressMapper.toAddress(placementOwnerDto.getAddress()));
        placementOwner
                .setRole(roleService.findByName(UserRole.PLACEMENT_OWNER));

        String password = placementOwnerDto.getPassword();
        if (password.length() == 60) {
            placementOwner.setPassword(password);
        } else {
            placementOwner.setPassword(bCryptPasswordEncoder.encode(password));
        }
        placementOwner.isLocked(placementOwnerDto.isLocked() == null ? false : placementOwnerDto.isLocked());

        return placementOwner;
    }

}

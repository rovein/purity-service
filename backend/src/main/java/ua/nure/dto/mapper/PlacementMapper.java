package ua.nure.dto.mapper;

import ua.nure.dto.PlacementDto;
import ua.nure.dto.SmartDeviceDto;
import ua.nure.entity.owner.Placement;
import ua.nure.entity.user.SmartDevice;

public class PlacementMapper {

    public static PlacementDto toPlacementDto(Placement placement) {
        return new PlacementDto()
                .setId(placement.getId())
                .setPlacementType(placement.getPlacementType())
                .setFloor(placement.getFloor())
                .setWindowsCount(placement.getWindowsCount())
                .setArea(placement.getArea())
                .setLastCleaning(placement.getLastCleaning())
                .setSmartDevice(SmartDeviceMapper.toSmartDeviceDto(placement.getSmartDevice()));
    }

    public static Placement toPlacement(PlacementDto placementDto) {
        SmartDeviceDto smartDeviceDto = placementDto.getSmartDevice();
        SmartDevice smartDevice = (smartDeviceDto == null) ? null : SmartDeviceMapper.toSmartDevice(smartDeviceDto);

        return new Placement()
                .setId(placementDto.getId() == null ? 0 : placementDto.getId())
                .setPlacementType(placementDto.getPlacementType())
                .setFloor(placementDto.getFloor())
                .setWindowsCount(placementDto.getWindowsCount())
                .setArea(placementDto.getArea())
                .setLastCleaning(placementDto.getLastCleaning())
                .setSmartDevice(smartDevice);

    }

}

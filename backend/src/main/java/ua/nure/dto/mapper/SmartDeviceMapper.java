package ua.nure.dto.mapper;

import ua.nure.dto.SmartDeviceDto;
import ua.nure.entity.user.SmartDevice;

public class SmartDeviceMapper {

    public static SmartDeviceDto toSmartDeviceDto(SmartDevice smartDevice) {

        if (smartDevice == null) {
            return new SmartDeviceDto();
        }

        return new SmartDeviceDto()
                .setId(smartDevice.getId())
                .setAirQuality(smartDevice.getAirQuality())
                .setTemperature(smartDevice.getTemperature())
                .setHumidity(smartDevice.getHumidity())
                .setAdjustmentFactor(smartDevice.getAdjustmentFactor())
                .setDirtinessFactor(smartDevice.getDirtinessFactor())
                .setPriority(smartDevice.getPriority());
    }

    public static SmartDevice toSmartDevice(SmartDeviceDto smartDeviceDto) {

        if (smartDeviceDto == null) {
            return new SmartDevice();
        }

        return new SmartDevice()
                .setAirQuality(smartDeviceDto.getAirQuality())
                .setTemperature(smartDeviceDto.getTemperature())
                .setHumidity(smartDeviceDto.getHumidity())
                .setAdjustmentFactor(smartDeviceDto.getAdjustmentFactor())
                .setDirtinessFactor(smartDeviceDto.getDirtinessFactor())
                .setPriority(smartDeviceDto.getPriority());
    }
}

package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SmartDeviceDto {

    private Long id;

    private double airQuality;

    private double temperature;

    private double humidity;

    private double adjustmentFactor;

    private double dirtinessFactor;

    private String priority;

}

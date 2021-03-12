package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PlacementDto {

    private Long id;

    private String placementType;

    private Integer floor;

    private Integer windowsCount;

    private Double area;

    private Date lastCleaning;

    private SmartDeviceDto smartDevice;

}

package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.dto.PlacementDto;
import ua.nure.dto.SmartDeviceDto;
import ua.nure.service.PlacementOwnerService;

@CrossOrigin
@RestController
@RequestMapping("/device")
@Api(tags = "Smart Device")
public class SmartDeviceController {

    @Autowired
    private PlacementOwnerService placementOwnerService;

    @PostMapping()
    @ApiOperation(
            value = "Update smart device characteristics, endpoint for Arduino",
            nickname = "updateSmartDevice"
    )
    public ResponseEntity<?> updateSmartDevice(
            @RequestBody SmartDeviceDto smartDeviceDto
    ) {
        PlacementDto placementDto =
                placementOwnerService.findPlacementById(smartDeviceDto.getId());

        if (placementDto == null) {
            return ResponseEntity.notFound().build();
        }

        PlacementDto updatedPlacement =
                placementOwnerService.updateSmartDevice(smartDeviceDto);
        System.out.println(updatedPlacement);

        return ResponseEntity.ok(updatedPlacement);
    }

}

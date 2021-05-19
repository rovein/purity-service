package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.dto.CleaningProviderDto;
import ua.nure.dto.PlacementOwnerDto;
import ua.nure.dto.PlacementDto;
import ua.nure.service.PlacementOwnerService;

import javax.validation.Valid;
import java.util.List;

import static ua.nure.validation.BindingResultValidator.errorBody;

@CrossOrigin
@RestController
@RequestMapping("/placement-owners")
@Api(tags = "Placement Owner")
public class PlacementOwnerController {
    
    private final PlacementOwnerService placementOwnerService;

    @Autowired
    public PlacementOwnerController(
            PlacementOwnerService placementOwnerService) {
        this.placementOwnerService = placementOwnerService;
    }

    @GetMapping
    @ApiOperation(
            value = "Returns a list of all placement owners", 
            nickname = "getAllPlacementOwners"
    )
    public ResponseEntity<List<PlacementOwnerDto>> getAllPlacementOwners() {
        return ResponseEntity.ok(placementOwnerService.findAll());
    }

    @GetMapping("/{email}")
    @ApiOperation(
            value = "Finds placement owner by email", 
            nickname = "getPlacementOwnerByEmail"
    )
    public ResponseEntity<?> getPlacementOwnerByEmail(
            @PathVariable String email
    ) {
        PlacementOwnerDto placementOwner = 
                placementOwnerService.findByEmail(email);
        if (placementOwner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(placementOwner);
    }

    @PostMapping
    @ApiOperation(
            value = "Adds new placement owner", 
            nickname = "addPlacementOwner"
    )
    public ResponseEntity<?> addPlacementOwner(
            @Valid @RequestBody PlacementOwnerDto placementOwnerDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }
        return ResponseEntity.ok(placementOwnerService.create(
                placementOwnerDto)
        );
    }

    @PutMapping
    @ApiOperation(
            value = "Updates the placement owner", 
            nickname = "updatePlacementOwner"
    )
    public ResponseEntity<?> updatePlacementOwner(
            @Valid @RequestBody PlacementOwnerDto placementOwnerDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        PlacementOwnerDto owner = placementOwnerService.findByEmail(
                placementOwnerDto.getEmail());

        if (owner== null) {
            return ResponseEntity.notFound().build();
        }
        String password = placementOwnerDto.getPassword();
        if (password == null || password.isEmpty()) {
            placementOwnerDto.setPassword(owner.getPassword());
        }
        PlacementOwnerDto updatedOwner = placementOwnerService.update(placementOwnerDto);

        return ResponseEntity.ok(updatedOwner);
    }

    @DeleteMapping("/{email}")
    @ApiOperation(
            value = "Deletes placement owner by email", 
            nickname = "deletePlacementOwner"
    )
    public ResponseEntity<Void> deletePlacementOwner(
            @PathVariable String email) {
        PlacementOwnerDto placementOwner = 
                placementOwnerService.findByEmail(email);

        if (placementOwner == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        placementOwnerService.delete(placementOwner);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{email}/placements")
    @ApiOperation(
            value = "Returns all placements", 
            nickname = "getAllPlacements"
    )
    public ResponseEntity<?> getAllPlacements(@PathVariable String email) {
        PlacementOwnerDto placementOwner = 
                placementOwnerService.findByEmail(email);
        if (placementOwner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(placementOwnerService.findAllPlacements(email));
    }

    @PostMapping("/{email}/placements")
    @ApiOperation(
            value = "Adds new placement for owner",
            nickname = "addPlacement"
    )
    public ResponseEntity<?> addPlacement(
            @PathVariable String email,
            @Valid @RequestBody PlacementDto placementDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        PlacementOwnerDto placementOwner = 
                placementOwnerService.findByEmail(email);

        if (placementOwner == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                placementOwnerService.addPlacement(placementDto, email)
        );
    }

    @PutMapping("/{email}/placements")
    @ApiOperation(
            value = "Updates placement owner (placement id must be present)", 
            nickname = "updatePlacement"
    )
    public ResponseEntity<?> updatePlacement(
            @PathVariable String email,
            @Valid @RequestBody PlacementDto placementDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        PlacementDto updatedPlacement = placementOwnerService.updatePlacement(
                placementDto, email);

        if (updatedPlacement == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPlacement);
    }

    @DeleteMapping("/placements/{id}")
    @ApiOperation(
            value = "Deletes placement by ID",
            nickname = "deletePlacement"
    )
    public ResponseEntity<Void> deletePlacement(@PathVariable Long id) {
        PlacementDto placement = placementOwnerService.findPlacementById(id);

        if (placement == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        placementOwnerService.deletePlacement(placement);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/placements/{id}")
    @ApiOperation(
            value = "Finds placement by id",
            nickname = "getPlacementById"
    )
    public ResponseEntity<?> getPlacementById(@PathVariable Long id) {
        PlacementDto placementDto = placementOwnerService.findPlacementById(id);
        if (placementDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(placementDto);
    }
    
}

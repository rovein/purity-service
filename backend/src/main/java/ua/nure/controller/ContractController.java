package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.dto.CleaningProviderDto;
import ua.nure.dto.ContractRequestDto;
import ua.nure.dto.ContractResponseDto;
import ua.nure.dto.PlacementOwnerDto;
import ua.nure.service.CleaningProviderService;
import ua.nure.service.ContractService;
import ua.nure.service.PlacementOwnerService;

import javax.validation.Valid;

import static ua.nure.validation.BindingResultValidator.errorBody;

@CrossOrigin
@RestController
@RequestMapping("/contracts")
@Api(tags = "Contract")
public class ContractController {

    private final ContractService contractService;

    private final CleaningProviderService cleaningProviderService;

    private final PlacementOwnerService placementOwnerService;

    @Autowired
    public ContractController(ContractService contractService,
            CleaningProviderService cleaningProviderService,
            PlacementOwnerService placementOwnerService) {
        this.contractService = contractService;
        this.cleaningProviderService = cleaningProviderService;
        this.placementOwnerService = placementOwnerService;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds contract by id", nickname = "getContractById")
    public ResponseEntity<?> getContractById(@PathVariable Long id) {
        ContractResponseDto contractResponseDto = contractService.findById(id);
        if (contractResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contractResponseDto);
    }

    @GetMapping("/cleaning-provider/{email}")
    @ApiOperation(
            value = "Finds all contracts for cleaning provider", 
            nickname = "getAllContractsByCleaningProvider"
    )
    public ResponseEntity<?> getAllContractsByCleaningProvider(
            @PathVariable String email) {
        CleaningProviderDto cleaningProvider = 
                cleaningProviderService.findByEmail(email);
        if (cleaningProvider == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok(contractService.getAllContractsByCleaningProvider(email));
    }

    @GetMapping("/placement-owner/{email}")
    @ApiOperation(
            value = "Finds all contracts for placement owner", 
            nickname = "getAllContractsByPlacementOwner"
    )
    public ResponseEntity<?> getAllContractsByPlacementOwner(
            @PathVariable String email) {
        PlacementOwnerDto placementOwner =
                placementOwnerService.findByEmail(email);
        if (placementOwner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok(contractService.getAllContractsByPlacementOwner(email));
    }

    @PostMapping
    @ApiOperation(value = "Creates new contract", nickname = "addContract")
    public ResponseEntity<?> addContract(
            @Valid @RequestBody ContractRequestDto contractRequestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        if (placementOwnerService
                .findPlacementById(
                        contractRequestDto.getPlacementId()) == null) {
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND)
                    .body("Placement does not exist");
        }

        if (cleaningProviderService
                .findProviderServiceById(
                        contractRequestDto.getProviderServiceId()) == null) {
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND)
                    .body("Provider service does not exist");
        }

        return ResponseEntity.ok(contractService.create(contractRequestDto));
    }

    @PutMapping
    @ApiOperation(
            value = "Updates contract "
                    + "(Contract ID must be present, updates only date)",
            nickname = "updateContract"
    )
    public ResponseEntity<?> updateContract(
            @Valid @RequestBody ContractRequestDto contractRequestDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        ContractResponseDto updatedContract =
                contractService.update(contractRequestDto);

        if (updatedContract == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedContract);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes contract by ID", nickname = "deleteContract")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        ContractResponseDto contractResponseDto = contractService.findById(id);

        if (contractResponseDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        contractService.delete(contractResponseDto.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

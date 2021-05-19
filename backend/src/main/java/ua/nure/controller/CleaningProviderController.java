package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.dto.CleaningProviderDto;
import ua.nure.dto.ProviderServiceDto;
import ua.nure.service.CleaningProviderService;

import javax.validation.Valid;

import java.util.List;

import static ua.nure.validation.BindingResultValidator.errorBody;

@CrossOrigin
@RestController
@RequestMapping("/cleaning-providers")
@Api(tags = "Cleaning Provider")
public class CleaningProviderController {

    private final CleaningProviderService cleaningProviderService;

    @Autowired
    public CleaningProviderController(
            CleaningProviderService cleaningProviderService) {
        this.cleaningProviderService = cleaningProviderService;
    }

    @GetMapping
    @ApiOperation(
            value = "Returns a list of all cleaning providers", 
            nickname = "getAllCleaningProviders"
    )
    public ResponseEntity<List<CleaningProviderDto>> getAllCleaningProviders() {
        return ResponseEntity.ok(cleaningProviderService.findAll());
    }

    @GetMapping("/{email}")
    @ApiOperation(
            value = "Finds cleaning provider by email", 
            nickname = "getCleaningProviderByEmail"
    )
    public ResponseEntity<?> getCleaningProviderByEmail(
            @PathVariable String email) {
        CleaningProviderDto cleaningProvider = 
                cleaningProviderService.findByEmail(email);
        if (cleaningProvider == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cleaningProvider);
    }

    @PostMapping
    @ApiOperation(value = "Adds new cleaning provider", nickname = "addCleaningProvider")
    public ResponseEntity<?> addCleaningProvider(
            @Valid @RequestBody CleaningProviderDto cleaningProviderDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }
        return ResponseEntity
                .ok(cleaningProviderService.create(cleaningProviderDto));
    }

    @PutMapping
    @ApiOperation(
            value = "Updates the cleaning provider", 
            nickname = "updateCleaningProvider"
    )
    public ResponseEntity<?> updateCleaningProvider(
            @Valid @RequestBody CleaningProviderDto cleaningProviderDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        CleaningProviderDto provider =
                cleaningProviderService.findByEmail(cleaningProviderDto.getEmail());

        if (provider == null) {
            return ResponseEntity.notFound().build();
        }

        String password = cleaningProviderDto.getPassword();
        if (password == null || password.isEmpty()) {
            cleaningProviderDto.setPassword(provider.getPassword());
        }
        CleaningProviderDto updatedProvider = cleaningProviderService.update(cleaningProviderDto);

        return ResponseEntity.ok(updatedProvider);
    }

    @DeleteMapping("/{email}")
    @ApiOperation(
            value = "Deletes cleaning provider by email", 
            nickname = "deleteCleaningProvider"
    )
    public ResponseEntity<Void> deleteCleaningProvider(
            @PathVariable String email) {
        CleaningProviderDto cleaningProvider = 
                cleaningProviderService.findByEmail(email);

        if (cleaningProvider == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        cleaningProviderService.delete(cleaningProvider);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{email}/services")
    @ApiOperation(
            value = "Returns all cleaning provider services (offers)", 
            nickname = "getAllProviderServices"
    )
    public ResponseEntity<?> getAllProviderServices(
            @PathVariable String email) {
        CleaningProviderDto cleaningProvider =
                cleaningProviderService.findByEmail(email);
        if (cleaningProvider == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok(cleaningProviderService.findAllProviderServices(email));
    }

    @PostMapping("/{email}/services")
    @ApiOperation(
            value = "Adds new service for cleaning provider",
            nickname = "addProviderService"
    )
    public ResponseEntity<?> addProviderService(@PathVariable String email,
            @Valid @RequestBody ProviderServiceDto providerServiceDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        CleaningProviderDto cleaningProvider =
                cleaningProviderService.findByEmail(email);

        if (cleaningProvider == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cleaningProviderService
                .addProviderService(providerServiceDto, email));
    }

    @PutMapping("/{email}/services")
    @ApiOperation(
            value = "Updates service of cleaning provider "
                    + "(service ID must be present!)",
            nickname = "updateProviderService"
    )
    public ResponseEntity<?> updateProviderService(@PathVariable String email,
            @Valid @RequestBody ProviderServiceDto providerServiceDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        ProviderServiceDto updatedProviderService = cleaningProviderService
                .updateProviderService(providerServiceDto, email);

        if (updatedProviderService == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedProviderService);
    }

    @DeleteMapping("/services/{id}")
    @ApiOperation(
            value = "Deletes provider service by ID",
            nickname = "deleteProviderService"
    )
    public ResponseEntity<Void> deleteProviderService(@PathVariable Long id) {
        ProviderServiceDto providerServiceDto =
                cleaningProviderService.findProviderServiceById(id);

        if (providerServiceDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        cleaningProviderService.deleteProviderService(providerServiceDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/services/{id}")
    @ApiOperation(
            value = "Finds provider service by id",
            nickname = "getProviderServiceById"
    )
    public ResponseEntity<?> getProviderServiceById(@PathVariable Long id) {
        ProviderServiceDto providerServiceDto = cleaningProviderService
                .findProviderServiceById(id);
        if (providerServiceDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(providerServiceDto);
    }

}

package ua.nure.service;

import ua.nure.dto.ContractInfoDto;
import ua.nure.dto.ContractRequestDto;
import ua.nure.dto.ContractResponseDto;

import java.util.Set;

public interface ContractService {

    ContractResponseDto create(ContractRequestDto contractRequestDto);

    ContractResponseDto update(ContractRequestDto contractRequestDto);

    void delete(Long id);

    ContractResponseDto findById(Long id);

    Set<ContractInfoDto> getAllContractsByPlacementOwner(String email);

    Set<ContractInfoDto> getAllContractsByCleaningProvider(String email);
}

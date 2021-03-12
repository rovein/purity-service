package ua.nure.dto.mapper;

import ua.nure.dto.ContractResponseDto;
import ua.nure.entity.user.Contract;

public class ContractMapper {

    public static ContractResponseDto toContractResponseDto(Contract contract) {
        return new ContractResponseDto()
                .setId(contract.getId())
                .setDate(contract.getDate())
                .setPrice(contract.getPrice())
                .setProviderServiceId(contract.getProviderService().getId())
                .setPlacementId(contract.getPlacement().getId());
    }
}

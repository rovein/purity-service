package ua.nure.dto.mapper;

import ua.nure.dto.CleaningProviderDto;
import ua.nure.entity.provider.CleaningProvider;
import ua.nure.entity.role.UserRole;

public class CleaningProviderMapper {

    public static CleaningProviderDto toCleaningProviderDto(CleaningProvider cleaningProvider) {
        return new CleaningProviderDto()
                .setId(cleaningProvider.getId())
                .setPhoneNumber(cleaningProvider.getPhoneNumber())
                .setEmail(cleaningProvider.getEmail())
                .setPassword(cleaningProvider.getPassword())
                .setName(cleaningProvider.getName())
                .setCreationDate(cleaningProvider.getCreationDate())
                .setRole(UserRole.CLEANING_PROVIDER)
                .isLocked(cleaningProvider.isLocked())
                .setAddress(AddressMapper.toAddressDto(cleaningProvider.getAddress()));
    }
}

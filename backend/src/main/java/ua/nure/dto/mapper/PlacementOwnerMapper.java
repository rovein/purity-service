package ua.nure.dto.mapper;

import ua.nure.dto.PlacementOwnerDto;
import ua.nure.entity.owner.PlacementOwner;
import ua.nure.entity.role.UserRole;

public class PlacementOwnerMapper {

    public static PlacementOwnerDto toPlacementOwnerDto(PlacementOwner placementOwner) {
        return new PlacementOwnerDto()
                .setId(placementOwner.getId())
                .setPhoneNumber(placementOwner.getPhoneNumber())
                .setEmail(placementOwner.getEmail())
                .setPassword(placementOwner.getPassword())
                .setName(placementOwner.getName())
                .setCreationDate(placementOwner.getCreationDate())
                .setRole(UserRole.PLACEMENT_OWNER)
                .setAddress(AddressMapper.toAddressDto(placementOwner.getAddress()));
    }

}

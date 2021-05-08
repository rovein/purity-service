package ua.nure.dto.mapper;

import ua.nure.dto.AddressDto;
import ua.nure.entity.user.Address;

public class AddressMapper {

    public static AddressDto toAddressDto(Address address) {
        return new AddressDto()
                .setCountry(address.getCountry())
                .setCity(address.getCity())
                .setStreet(address.getStreet())
                .setHouseNumber(address.getHouseNumber())
                .setLatitude(address.getLatitude())
                .setLongitude(address.getLongitude());
    }

    public static Address toAddress(AddressDto addressDto) {
        if (addressDto == null) {
            return new Address();
        }
        return new Address()
                .setCountry(addressDto.getCountry())
                .setCity(addressDto.getCity())
                .setStreet(addressDto.getStreet())
                .setHouseNumber(addressDto.getHouseNumber())
                .setLatitude(addressDto.getLatitude())
                .setLongitude(addressDto.getLongitude());
    }
}

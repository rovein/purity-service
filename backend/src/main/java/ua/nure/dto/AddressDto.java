package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AddressDto {

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String latitude;

    private String longitude;

}

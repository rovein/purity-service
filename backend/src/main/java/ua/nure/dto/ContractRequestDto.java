package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ContractRequestDto {

    private Long id;

    private Date date;

    private Long placementId;

    private Long providerServiceId;

}

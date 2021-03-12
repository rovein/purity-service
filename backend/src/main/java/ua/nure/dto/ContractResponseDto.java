package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ContractResponseDto {

    private Long Id;

    private Date date;

    private Double price;

    private Long placementId;

    private Long providerServiceId;
}

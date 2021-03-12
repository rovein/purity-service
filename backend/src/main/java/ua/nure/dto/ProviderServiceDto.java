package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ProviderServiceDto {
    private Long id;

    private String name;

    private String description;

    private Integer minArea;

    private Integer maxArea;

    private String placementType;

    private Double pricePerMeter;
}

package ua.nure.dto.mapper;

import ua.nure.dto.ProviderServiceDto;
import ua.nure.entity.provider.ProviderService;

public class ProviderServiceMapper {

    public static ProviderServiceDto toProviderServiceDto(
            ProviderService providerService) {
        return new ProviderServiceDto().setId(providerService.getId())
                .setName(providerService.getName())
                .setDescription(providerService.getDescription())
                .setMinArea(providerService.getMinArea())
                .setMaxArea(providerService.getMaxArea())
                .setPlacementType(providerService.getRoomType())
                .setPricePerMeter(providerService.getPricePerMeter());

    }

    public static ProviderService toProviderService(
            ProviderServiceDto providerServiceDto) {
        return new ProviderService().setId(
                providerServiceDto.getId() == null
                        ? 0
                        : providerServiceDto.getId()
        )
                .setName(providerServiceDto.getName())
                .setDescription(providerServiceDto.getDescription())
                .setMinArea(providerServiceDto.getMinArea())
                .setMaxArea(providerServiceDto.getMaxArea())
                .setRoomType(providerServiceDto.getPlacementType())
                .setPricePerMeter(providerServiceDto.getPricePerMeter());

    }
}

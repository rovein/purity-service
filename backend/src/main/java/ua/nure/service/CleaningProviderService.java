package ua.nure.service;

import ua.nure.dto.CleaningProviderDto;
import ua.nure.dto.ProviderServiceDto;

import java.util.List;
import java.util.Set;

public interface CleaningProviderService {

    CleaningProviderDto create(CleaningProviderDto cleaningProviderDto);

    CleaningProviderDto update(CleaningProviderDto cleaningProviderDto);

    void delete(CleaningProviderDto cleaningProviderDto);

    List<CleaningProviderDto> findAll();

    CleaningProviderDto findByPhoneNumber(String phoneNumber);

    CleaningProviderDto findByEmail(String email);

    CleaningProviderDto findById(Long id);

    Set<ProviderServiceDto> findAllProviderServices(String email);

    ProviderServiceDto addProviderService(ProviderServiceDto providerServiceDto, String email);

    ProviderServiceDto updateProviderService(
            ProviderServiceDto providerServiceDto, String email);

    void deleteProviderService(ProviderServiceDto providerServiceDto);

    ProviderServiceDto findProviderServiceById(Long id);
}

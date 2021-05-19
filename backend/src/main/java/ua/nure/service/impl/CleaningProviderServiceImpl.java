package ua.nure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nure.dto.CleaningProviderDto;
import ua.nure.dto.ProviderServiceDto;
import ua.nure.dto.mapper.AddressMapper;
import ua.nure.dto.mapper.CleaningProviderMapper;
import ua.nure.dto.mapper.ProviderServiceMapper;
import ua.nure.entity.provider.CleaningProvider;
import ua.nure.entity.provider.ProviderService;
import ua.nure.entity.role.UserRole;
import ua.nure.repository.CleaningProviderRepository;
import ua.nure.repository.ProviderServiceRepository;
import ua.nure.service.CleaningProviderService;
import ua.nure.service.RoleService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ua.nure.dto.mapper.ProviderServiceMapper.toProviderServiceDto;

@Service
public class CleaningProviderServiceImpl implements CleaningProviderService {

    private final CleaningProviderRepository cleaningProviderRepository;

    private final RoleService roleService;

    private final ProviderServiceRepository providerServiceRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CleaningProviderServiceImpl(
            CleaningProviderRepository cleaningProviderRepository,
            RoleService roleService,
            ProviderServiceRepository providerServiceRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.cleaningProviderRepository = cleaningProviderRepository;
        this.roleService = roleService;
        this.providerServiceRepository = providerServiceRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public CleaningProviderDto create(CleaningProviderDto cleaningProviderDto) {
        CleaningProvider cleaningProvider = toCleaningProvider(
                cleaningProviderDto, new CleaningProvider());

        return CleaningProviderMapper.toCleaningProviderDto(
                cleaningProviderRepository.save(cleaningProvider)
        );
    }

    @Override
    public CleaningProviderDto update(CleaningProviderDto cleaningProviderDto) {
        Optional<CleaningProvider> provider = cleaningProviderRepository
                .findByEmail(cleaningProviderDto.getEmail());

        if (provider.isPresent()) {
            CleaningProvider cleaningProviderModel = toCleaningProvider(
                    cleaningProviderDto, provider.get());

            return CleaningProviderMapper.toCleaningProviderDto(
                    cleaningProviderRepository.save(cleaningProviderModel));
        }

        return null;
    }

    @Override
    public void delete(CleaningProviderDto cleaningProviderDto) {
        cleaningProviderRepository.deleteById(cleaningProviderDto.getId());
    }

    @Override
    public List<CleaningProviderDto> findAll() {
        List<CleaningProvider> cleaningProviders = 
                cleaningProviderRepository.findAll();
        List<CleaningProviderDto> cleaningProvidersDto = new ArrayList<>();

        cleaningProviders.forEach(provider -> cleaningProvidersDto.add(
                CleaningProviderMapper.toCleaningProviderDto(provider))
        );
        
        return cleaningProvidersDto;
    }

    @Override
    public CleaningProviderDto findByPhoneNumber(String phoneNumber) {
        Optional<CleaningProvider> cleaningProvider = cleaningProviderRepository
                .findByPhoneNumber(phoneNumber);
        return cleaningProvider
                .map(CleaningProviderMapper::toCleaningProviderDto)
                .orElse(null);
    }

    @Override
    public CleaningProviderDto findByEmail(String email) {
        Optional<CleaningProvider> cleaningProvider = cleaningProviderRepository
                .findByEmail(email);
        return cleaningProvider
                .map(CleaningProviderMapper::toCleaningProviderDto)
                .orElse(null);
    }

    @Override
    public CleaningProviderDto findById(Long id) {
        Optional<CleaningProvider> cleaningProvider = cleaningProviderRepository
                .findById(id);
        return cleaningProvider
                .map(CleaningProviderMapper::toCleaningProviderDto)
                .orElse(null);
    }

    @Override
    public Set<ProviderServiceDto> findAllProviderServices(String email) {
        Optional<CleaningProvider> provider = cleaningProviderRepository
                .findByEmail(email);
        if (provider.isPresent()) {
            Set<ProviderService> providerServices = 
                    provider.get().getProviderServices();
            Set<ProviderServiceDto> providerServicesDto = new HashSet<>();

            providerServices.forEach(providerService -> providerServicesDto
                    .add(toProviderServiceDto(providerService))
            );
            
            return providerServicesDto;
        }
        return Collections.emptySet();
    }

    @Override
    public ProviderServiceDto addProviderService(
            ProviderServiceDto providerServiceDto, String email) {
        Optional<CleaningProvider> cleaningProvider = cleaningProviderRepository
                .findByEmail(email);

        ProviderService providerService = ProviderServiceMapper
                .toProviderService(providerServiceDto);

        cleaningProvider.ifPresent(providerService::setCleaningProvider);

        return toProviderServiceDto(
                providerServiceRepository.save(providerService)
        );
    }

    @Override
    public ProviderServiceDto updateProviderService(
            ProviderServiceDto providerServiceDto, String email) {
        Optional<CleaningProvider> cleaningProvider = cleaningProviderRepository
                .findByEmail(email);

        if (cleaningProvider.isPresent()) {
            Optional<ProviderService> optionalService = providerServiceRepository
                    .findById(providerServiceDto.getId());

            if (optionalService.isPresent()) {
                ProviderService providerService = ProviderServiceMapper
                        .toProviderService(providerServiceDto);
                cleaningProvider.ifPresent(providerService::setCleaningProvider);

                return toProviderServiceDto(
                        providerServiceRepository.save(providerService)
                );
            }
        }

        return null;
    }

    @Override
    public void deleteProviderService(ProviderServiceDto providerServiceDto) {
        providerServiceRepository.deleteById(providerServiceDto.getId());
    }

    @Override
    public ProviderServiceDto findProviderServiceById(Long id) {
        Optional<ProviderService> providerService = providerServiceRepository
                .findById(id);
        return providerService.map(ProviderServiceMapper::toProviderServiceDto)
                .orElse(null);
    }

    private CleaningProvider toCleaningProvider(
            CleaningProviderDto cleaningProviderDto,
            CleaningProvider cleaningProvider
    ) {
        cleaningProvider.setEmail(cleaningProviderDto.getEmail());
        cleaningProvider.setPhoneNumber(cleaningProviderDto.getPhoneNumber());
        cleaningProvider.setName(cleaningProviderDto.getName());
        cleaningProvider.setCreationDate(cleaningProviderDto.getCreationDate());
        cleaningProvider.setAddress(
                AddressMapper.toAddress(cleaningProviderDto.getAddress()));
        cleaningProvider
                .setRole(roleService.findByName(UserRole.CLEANING_PROVIDER));

        String password = cleaningProviderDto.getPassword();
        if (password.length() == 60) {
            cleaningProvider.setPassword(password);
        } else {
          cleaningProvider.setPassword(bCryptPasswordEncoder.encode(password));
        }
        cleaningProvider.isLocked(cleaningProviderDto.isLocked() == null ? false : cleaningProviderDto.isLocked());

        return cleaningProvider;
    }

}

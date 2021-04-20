package ua.nure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.dto.ContractInfoDto;
import ua.nure.dto.ContractRequestDto;
import ua.nure.dto.ContractResponseDto;
import ua.nure.dto.mapper.ContractMapper;
import ua.nure.entity.owner.Placement;
import ua.nure.entity.provider.CleaningProvider;
import ua.nure.entity.owner.PlacementOwner;
import ua.nure.entity.provider.ProviderService;
import ua.nure.entity.user.Address;
import ua.nure.entity.user.Contract;
import ua.nure.entity.user.SmartDevice;
import ua.nure.repository.*;
import ua.nure.service.ContractService;
import ua.nure.util.EmailUtil;

import com.google.common.io.Files;
import ua.nure.util.PathsUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    private final PlacementRepository placementRepository;

    private final ProviderServiceRepository providerServiceRepository;

    private final CleaningProviderRepository cleaningProviderRepository;

    private final PlacementOwnerRepository placementOwnerRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository,
            PlacementRepository placementRepository,
            ProviderServiceRepository providerServiceRepository,
            CleaningProviderRepository cleaningProviderRepository,
            PlacementOwnerRepository placementOwnerRepository) {
        this.contractRepository = contractRepository;
        this.placementRepository = placementRepository;
        this.providerServiceRepository = providerServiceRepository;
        this.cleaningProviderRepository = cleaningProviderRepository;
        this.placementOwnerRepository = placementOwnerRepository;
    }

    @Override
    public ContractResponseDto create(ContractRequestDto contractRequestDto) {
        Contract contract = new Contract();
        contract.setDate(contractRequestDto.getDate());

        Optional<Placement> placementById = placementRepository
                .findById(contractRequestDto.getPlacementId());
        Optional<ProviderService> providerServiceById =
                providerServiceRepository.findById(
                        contractRequestDto.getProviderServiceId()
                );

        if (placementById.isPresent() && providerServiceById.isPresent()) {
            Placement placement = placementById.get();
            ProviderService providerService = providerServiceById.get();

            contract.setPlacement(placement);
            contract.setProviderService(providerService);

            double price =
                    providerService.getPricePerMeter() * placement.getArea();
            Double priceFactor = null;

            SmartDevice smartDevice = placement.getSmartDevice();

            if (isPresent(smartDevice))
                priceFactor = smartDevice.getAdjustmentFactor();

            if (isPresent(priceFactor)) {
                if (priceFactor != 0) {
                    price = price * priceFactor;
                }
            }
            contract.setPrice(price);
            String html = "Тут мала бути інформація про складену угоду, але щось пішло не так...";
            String content;
            try {
                Path filePath = PathsUtil.getResourcePath("email-templates/contract-created-message.html");
                html = Files.asCharSource(new File(filePath.toString()), StandardCharsets.UTF_8).read();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                content = html;
            }

            PlacementOwner owner = placement.getPlacementOwner();
            CleaningProvider provider = providerService.getCleaningProvider();
            Address address = owner.getAddress();

            Date date = contract.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            new Thread(() -> Arrays.asList(owner.getEmail(), provider.getEmail())
                    .forEach(email -> EmailUtil.message()
                            .destination(email)
                            .subject("Нова угода на прибирання")
                            .body(String.format(content,
                                    dateFormat.format(date),
                                    timeFormat.format(date),
                                    contract.getPlacement().getId(),
                                    contract.getProviderService().getName(),
                                    contract.getPrice(),
                                    address.getCountry(),
                                    address.getCity(),
                                    address.getStreet(),
                                    address.getHouseNumber(),
                                    provider.getName(),
                                    provider.getEmail(),
                                    provider.getPhoneNumber(),
                                    owner.getName(),
                                    owner.getEmail(),
                                    owner.getPhoneNumber())
                            ).send())
            ).start();

            return ContractMapper.toContractResponseDto(contractRepository.save(contract));
        }

        return null;
    }

    @Override
    public ContractResponseDto update(ContractRequestDto contractRequestDto) {
        Optional<Contract> contractById = contractRepository
                .findById(contractRequestDto.getId());

        if (contractById.isPresent()) {
            Contract contract = contractById.get();

            contract.setDate(contractRequestDto.getDate());
            return ContractMapper
                    .toContractResponseDto(contractRepository.save(contract));
        }

        return null;
    }

    @Override
    public void delete(Long id) {
        contractRepository.deleteById(id);
    }

    @Override
    public ContractResponseDto findById(Long id) {
        Optional<Contract> contract = contractRepository.findById(id);
        return contract.map(ContractMapper::toContractResponseDto)
                .orElse(null);
    }

    @Override
    public Set<ContractInfoDto> getAllContractsByPlacementOwner(String email) {
        Optional<PlacementOwner> customerCompanyOptional =
                placementOwnerRepository.findByEmail(email);
        if (customerCompanyOptional.isPresent()) {
            return contractRepository.getAllContractsByCustomerCompany(
                    customerCompanyOptional.get().getEmail()
            );
        }

        return Collections.emptySet();
    }

    @Override
    public Set<ContractInfoDto> getAllContractsByCleaningProvider(String email) {
        Optional<CleaningProvider> cleaningProviderOptional =
                cleaningProviderRepository.findByEmail(email);
        if (cleaningProviderOptional.isPresent()) {
            return contractRepository.getAllContractsByCleaningCompany(
                    cleaningProviderOptional.get().getEmail()
            );
        }

        return Collections.emptySet();
    }

    private boolean isPresent (Object object) {
        return object != null;
    }
}

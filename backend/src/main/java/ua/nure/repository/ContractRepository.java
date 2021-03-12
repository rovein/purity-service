package ua.nure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.dto.ContractInfoDto;
import ua.nure.entity.user.Contract;

import java.util.Set;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {

    @Query(
            value = "SELECT con.contract_id as id, con.date as date, con.price as price, service.name as serviceName," +
                    " cl_p.name as cleaningProviderName, pl_own.name as placementOwnerName, placement.placement_id as placementId, " +
                    "service.provider_service_id as providerServiceId " +
                    "FROM contract con, provider_service service, placement, cleaning_provider cl_p, placement_owner pl_own " +
                    "WHERE pl_own.email = ?1 AND con.placement_id = placement.placement_id and con.provider_service_id = service.provider_service_id " +
                    "and service.cleaning_provider_id = cl_p.cleaning_provider_id and placement.placement_owner_id = pl_own.placement_owner_id",
            nativeQuery = true
    )
    Set<ContractInfoDto> getAllContractsByCustomerCompany(String email);

    @Query(
            value = "SELECT con.contract_id as id, con.date as date, con.price as price, service.name as serviceName," +
                    " cl_p.name as cleaningProviderName, pl_own.name as placementOwnerName, placement.placement_id as placementId, " +
                    "service.provider_service_id as providerServiceId " +
                    "FROM contract con, provider_service service, placement, cleaning_provider cl_p, placement_owner pl_own " +
                    "WHERE cl_p.email = ?1 AND con.placement_id = placement.placement_id and con.provider_service_id = service.provider_service_id " +
                    "and service.cleaning_provider_id = cl_p.cleaning_provider_id and placement.placement_owner_id = pl_own.placement_owner_id",
            nativeQuery = true
    )
    Set<ContractInfoDto> getAllContractsByCleaningCompany(String email);

}

package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.provider.ProviderService;

@Repository
public interface ProviderServiceRepository extends CrudRepository<ProviderService, Long> {

}

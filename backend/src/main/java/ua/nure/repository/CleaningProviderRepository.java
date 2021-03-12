package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.provider.CleaningProvider;

import java.util.List;
import java.util.Optional;

@Repository
public interface CleaningProviderRepository extends CrudRepository<CleaningProvider, Long> {

    Optional<CleaningProvider> findByPhoneNumber(String phoneNumber);

    Optional<CleaningProvider> findByEmail(String email);

    List<CleaningProvider> findAll();

    Optional<CleaningProvider> findByName(String name);



}

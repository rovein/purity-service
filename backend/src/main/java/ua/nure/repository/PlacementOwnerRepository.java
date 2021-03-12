package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.owner.PlacementOwner;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlacementOwnerRepository extends CrudRepository<PlacementOwner, Long> {

    Optional<PlacementOwner> findByPhoneNumber(String phoneNumber);

    Optional<PlacementOwner> findByEmail(String email);

    List<PlacementOwner> findAll();

}

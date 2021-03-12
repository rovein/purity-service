package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.owner.Placement;

@Repository
public interface PlacementRepository extends CrudRepository<Placement, Long> {

}

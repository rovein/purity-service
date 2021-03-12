package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.role.Role;
import ua.nure.entity.role.UserRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);

    List<Role> findAll();
}

package ua.nure.service;

import ua.nure.entity.role.Role;
import ua.nure.entity.role.UserRole;

public interface RoleService {

    Role findById(Long id);

    Role findByName(UserRole name);

}

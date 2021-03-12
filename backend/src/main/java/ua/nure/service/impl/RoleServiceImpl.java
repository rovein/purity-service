package ua.nure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.entity.role.Role;
import ua.nure.entity.role.UserRole;
import ua.nure.repository.RoleRepository;
import ua.nure.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findByName(UserRole name) {
        return roleRepository.findByName(name).orElse(null);
    }
}

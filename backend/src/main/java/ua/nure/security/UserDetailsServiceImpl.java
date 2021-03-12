package ua.nure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nure.entity.user.Admin;
import ua.nure.entity.provider.CleaningProvider;
import ua.nure.entity.owner.PlacementOwner;
import ua.nure.repository.AdminRepository;
import ua.nure.repository.CleaningProviderRepository;
import ua.nure.repository.PlacementOwnerRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PlacementOwnerRepository placementOwnerRepository;

    private final CleaningProviderRepository cleaningProviderRepository;

    private final AdminRepository adminRepository;

    @Autowired
    public UserDetailsServiceImpl(
            PlacementOwnerRepository placementOwnerRepository,
            CleaningProviderRepository cleaningProviderRepository,
            AdminRepository adminRepository
    ) {
        this.placementOwnerRepository = placementOwnerRepository;
        this.cleaningProviderRepository = cleaningProviderRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Optional<CleaningProvider> cleaningProviderUser =
                cleaningProviderRepository.findByEmail(email);
        Optional<PlacementOwner> placementOwnerUser =
                placementOwnerRepository.findByEmail(email);
        Optional<Admin> adminUser = adminRepository.findByEmail(email);

        if (cleaningProviderUser.isPresent()) {
            return new UserDetailsImpl(cleaningProviderUser.get());
        }

        if (placementOwnerUser.isPresent()) {
            return new UserDetailsImpl(placementOwnerUser.get());
        }

        if (adminUser.isPresent()) {
            return new UserDetailsImpl(adminUser.get());
        }

        throw new UsernameNotFoundException(
                "User with email " + email + " not found");
    }
}

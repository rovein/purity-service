package ua.nure.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.nure.repository.CleaningProviderRepository;
import ua.nure.repository.PlacementOwnerRepository;
import ua.nure.validation.annotation.UniqueEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private PlacementOwnerRepository placementOwnerRepository;

    @Autowired
    private CleaningProviderRepository cleaningProviderRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (placementOwnerRepository.findByEmail(value).isPresent()) {
            return false;
        } else
            return !cleaningProviderRepository.findByEmail(value).isPresent();
    }
}

package ua.nure.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ua.nure.repository.CleaningProviderRepository;
import ua.nure.repository.PlacementOwnerRepository;
import ua.nure.validation.annotation.UniquePhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {

    @Autowired
    private PlacementOwnerRepository placementOwnerRepository;

    @Autowired
    private CleaningProviderRepository cleaningProviderRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (placementOwnerRepository.findByPhoneNumber(value).isPresent()) {
            return false;
        } else
            return !cleaningProviderRepository.findByPhoneNumber(value).isPresent();
    }
}

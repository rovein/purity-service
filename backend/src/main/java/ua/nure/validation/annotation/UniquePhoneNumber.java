package ua.nure.validation.annotation;

import ua.nure.validation.UniquePhoneNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePhoneNumberValidator.class)
public @interface UniquePhoneNumber {
    String message() default "Phone number already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

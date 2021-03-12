package ua.nure.validation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

public class BindingResultValidator {

    public static Map<String, String> errorBody(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors()
                .forEach(fieldError ->
                        errors.put(
                                fieldError.getField(),
                                fieldError.getDefaultMessage())
                );

        bindingResult.getGlobalErrors()
                .forEach(globalError ->
                        errors.put(
                                globalError.getCode(),
                                globalError.getDefaultMessage())
                );

        return errors;
    }
}

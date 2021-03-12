package ua.nure.validation;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.lookups.v1.PhoneNumber;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import ua.nure.validation.annotation.ValidPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Configurable
@Component
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String twilioAccountSid = "ACf67452d588ce0927dd8785e32244214b";
        String twilioAuthToken = "5eaaa6b10613192a5a2b72595ce08d82";

        Twilio.init(twilioAccountSid, twilioAuthToken);

        // The Lookup API requires your phone number in E.164 format
        // E.164 formatted phone numbers must not have spaces in them
        value = value.replaceAll("[\\s()-]", "");

        if ("".equals(value)) {
            return false;
        }

        try {
            PhoneNumber.fetcher(new com.twilio.type.PhoneNumber(value)).fetch();
            return true;

        } catch (ApiException e) {
            // The Lookup API returns HTTP 404 if the phone number is not found
            // (ie it is not a real phone number)
            if (e.getStatusCode() == 404) {
                return false;
            }
            throw e;
        }
    }
}

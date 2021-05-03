package ua.nure.cleaningservice.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;

import ua.nure.cleaningservice.R;


public class Verification {

    public static boolean verifyName(Context context, EditText input) {
        return true;
    }

    public static boolean verifyEmail(Context context, EditText input) {
        String email = input.getText().toString();
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (pattern.matcher(email).matches()) {
            return true;
        } else {
            input.setError(context.getString(R.string.email_tip));
            return false;
        }
    }

    public static boolean verifyPassword(Context context, EditText input) {
        String password = input.getText().toString();
        Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9_-]{8,24}");
        if (!TextUtils.isEmpty(password) && PASSWORD_PATTERN.matcher(password).matches()) {
            return true;
        } else {
            input.setError(context.getString(R.string.password_tip));
            return false;
        }
    }

    public static boolean verifyPasswords(Context context, EditText passwordInput, EditText confirmPasswordInput) {
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            confirmPasswordInput.setError(context.getString(R.string.passwords_tip));
            return false;
        }
    }

    public static boolean verifyPhone(Context context, EditText input) {
        String phone = input.getText().toString();
        Pattern PHONE_PATTER = Pattern.compile("^[+]*[(]?[0-9]{1,4}[)]?[-s./0-9]*$");
        if (!TextUtils.isEmpty(phone) && PHONE_PATTER.matcher(phone).matches()) {
            return true;
        } else {
            input.setError(context.getString(R.string.phone_tip));
            return false;
        }
    }
}

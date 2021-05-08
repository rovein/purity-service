package ua.nure.cleaningservice.util

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import ua.nure.cleaningservice.R
import java.util.regex.Pattern

object Verification {
    fun verifyName(context: Context?, input: EditText?): Boolean {
        return true
    }

    fun verifyEmail(context: Context, input: EditText): Boolean {
        val email = input.text.toString()
        val pattern = Patterns.EMAIL_ADDRESS
        return if (pattern.matcher(email).matches()) {
            true
        } else {
            input.error = context.getString(R.string.email_tip)
            false
        }
    }

    fun verifyPassword(context: Context, input: EditText): Boolean {
        val password = input.text.toString()
        val PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9_-]{8,24}")
        return if (!TextUtils.isEmpty(password) && PASSWORD_PATTERN.matcher(password).matches()) {
            true
        } else {
            input.error = context.getString(R.string.password_tip)
            false
        }
    }

    fun verifyPasswords(
        context: Context,
        passwordInput: EditText,
        confirmPasswordInput: EditText
    ): Boolean {
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()
        return if (password == confirmPassword) {
            true
        } else {
            confirmPasswordInput.error = context.getString(R.string.passwords_tip)
            false
        }
    }

    fun verifyPhone(context: Context, input: EditText): Boolean {
        val phone = input.text.toString()
        val PHONE_PATTER = Pattern.compile("^[+]*[(]?[0-9]{1,4}[)]?[-s./0-9]*$")
        return if (!TextUtils.isEmpty(phone) && PHONE_PATTER.matcher(phone).matches()) {
            true
        } else {
            input.error = context.getString(R.string.phone_tip)
            false
        }
    }
}

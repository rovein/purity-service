package ua.nure.cleaningservice.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.Address
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.util.LoadingDialog
import ua.nure.cleaningservice.util.InternetConnection
import ua.nure.cleaningservice.util.Verification
import java.util.*

class SignUpActivity : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var phoneNumber: EditText
    lateinit var password: EditText
    lateinit var confirmPassword: EditText
    lateinit var confirmButton: Button
    lateinit var role: Spinner
    lateinit var goToLogin: LinearLayout
    lateinit var roles: ArrayList<String>
    lateinit var user: List<User>
    lateinit var context: Context
    private val loadingDialog = LoadingDialog(this@SignUpActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_sign_up)
        context = this
        init()
    }

    private fun init() {
        name = findViewById(R.id.name)
        role = findViewById(R.id.get_role_spinner)
        roles = ArrayList()
        roles.add(getString(R.string.placement_owner))
        roles.add(getString(R.string.cleaning_provider))

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        role.adapter = adapter
        name.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                Verification.verifyName(this, name)
            }
        }

        email = findViewById(R.id.email)
        email.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                Verification.verifyEmail(this, email)
            }
        }

        phoneNumber = findViewById(R.id.phone)
        phoneNumber.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                Verification.verifyPhone(this, phoneNumber)
            }
        }

        password = findViewById(R.id.password)
        password.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                Verification.verifyPassword(this, password)
            }
        }

        confirmPassword = findViewById(R.id.confirm_password)
        confirmPassword.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                Verification.verifyPasswords(this, password, confirmPassword)
            }
        }

        confirmButton = findViewById(R.id.signup_button)
        confirmButton.setOnClickListener {
            signUp(
                name.text.toString(),
                email.text.toString(),
                phoneNumber.text.toString(),
                password.text.toString(),
                role.selectedItem.toString()
            )
        }

        goToLogin = findViewById(R.id.go_to_sign_in)
        goToLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(name: String, email: String, phone: String, password: String, role: String) {
        if (!Verification.verifyName(this, this.name)
                || !Verification.verifyEmail(this, this.email)
                || !Verification.verifyPhone(this, phoneNumber)
                || !Verification.verifyPassword(this, this.password)
                || !Verification.verifyPasswords(this, this.password, confirmPassword)) {
            return
        } else if (!InternetConnection.checkConnection(applicationContext)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
            return
        } else {
            loadingDialog.start()
            val user = User.getInstance()
                    .setName(name)
                    .setEmail(email)
                    .setPhoneNumber(phone)
                    .setPassword(password)
                    .setUserRole(role)
            if (role == getString(R.string.placement_owner)) {
                NetworkService.getInstance()
                        .apiService
                        .placementOwnerSignUp(user)
                        .enqueue(signUpCallback)
            } else if (role == getString(R.string.cleaning_provider)) {
                NetworkService.getInstance()
                        .apiService
                        .cleaningProviderSignUp(user)
                        .enqueue(signUpCallback)
            }
        }
    }

    private val signUpCallback: Callback<User?> = object : Callback<User?> {
        override fun onResponse(call: Call<User?>, response: Response<User?>) {
            if (!response.isSuccessful) {
                Log.i(TAG, response.message() + " " + response.code())
                Toast.makeText(
                        this@SignUpActivity,
                        response.message(),
                        Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this@SignUpActivity,
                        SignInActivity::class.java)
                startActivity(intent)
            }
            loadingDialog.dismiss()
        }

        override fun onFailure(call: Call<User?>, t: Throwable) {
            Log.i(TAG, t.message!!)
            Toast.makeText(
                    this@SignUpActivity,
                    t.message,
                    Toast.LENGTH_SHORT
            ).show()
            loadingDialog.dismiss()
        }
    }

    companion object {
        private const val TAG = "SignUpActivity"
    }
}

package ua.nure.cleaningservice.ui.auth

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
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.util.LoadingDialogUtil
import ua.nure.cleaningservice.util.InternetConnection
import ua.nure.cleaningservice.util.Verification

class SignInActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    private lateinit var confirm: Button
    private lateinit var goToSignUp: Button
    private val loadingDialog = LoadingDialogUtil(this@SignInActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_sign_in)
        init()
    }

    private fun init() {
        email = findViewById(R.id.email)
        email.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                Verification.verifyEmail(this, email)
            }
        }
        password = findViewById(R.id.password)
        password.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                Verification.verifyPassword(this, password)
            }
        }
        confirm = findViewById(R.id.signin_button)
        confirm.setOnClickListener(View.OnClickListener {
            signIn(email.text.toString(), password.text.toString())
        })

        goToSignUp = findViewById(R.id.signup_button)
        goToSignUp.setOnClickListener {
            val intent = Intent(
                this@SignInActivity,
                SignUpActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        if (!Verification.verifyEmail(this, this.email) ||
                !Verification.verifyPassword(this, this.password)) {
            return
        } else if (!InternetConnection.checkConnection(applicationContext)) {
            Toast.makeText(
                    this,
                    R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show()
        } else {
            val company = User.getInstance()
                    .setEmail(email)
                    .setPassword(password)
            loadingDialog.start()
            NetworkService.getInstance()
                    .apiService
                    .login(company)
                    .enqueue(loginCallback)
        }
    }

    private val loginCallback: Callback<User> = object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            if (!response.isSuccessful) {
                Log.i(TAG, "in onResponse - !successful")
                Log.i(TAG, response.toString())
                Toast.makeText(
                    this@SignInActivity,
                    resources.getString(R.string.invalidCreds),
                    Toast.LENGTH_LONG).show()
            } else {
                Log.i(TAG, "in onResponse - LOG")
                Log.i(TAG, response.message())
                Log.i(TAG, response.body()!!.token)
                User.getInstance()
                        .setToken(response.body()!!.token).userRole = response.body()!!.userRole
                val intent = Intent(
                        this@SignInActivity,
                        MenuActivity::class.java
                )
                startActivity(intent)
            }
            loadingDialog.dismiss()
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
            Log.i(TAG, "in onFailure- LOG")
            Log.i(TAG, t.toString())
            println(t.message)
            Toast.makeText(
                    this@SignInActivity,
                    t.message,
                    Toast.LENGTH_SHORT).show()
            loadingDialog.dismiss()
        }

    }

    companion object {
        private const val TAG = "SignInActivity"
    }
}

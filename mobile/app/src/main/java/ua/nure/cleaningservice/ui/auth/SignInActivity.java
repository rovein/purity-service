package ua.nure.cleaningservice.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.util.InternetConnection;
import ua.nure.cleaningservice.util.Verification;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    EditText email, password;
    Button confirm;
    LinearLayout goToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_sign_in);

        init();
    }

    private void init() {

        email = findViewById(R.id.email);
        email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Verification.verifyEmail(this, email);
            }
        });

        password = findViewById(R.id.password);
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Verification.verifyPassword(this, password);
            }
        });

        confirm = findViewById(R.id.signup_button);
        confirm.setOnClickListener(v -> signIn(email.getText().toString(),
                password.getText().toString()));

        goToSignUp = findViewById(R.id.go_to_sign_in);
        goToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this,
                    SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void signIn(String email, String password) {
        if (!Verification.verifyEmail(this, this.email) ||
                !Verification.verifyPassword(this, this.password)) {
            return;
        } else if (!InternetConnection.checkConnection(getApplicationContext())) {
            Toast.makeText(
                    this,
                    R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show();
        } else {
            Company company = Company.getInstance()
                    .setEmail(email)
                    .setPassword(password);

            NetworkService.getInstance()
                    .getApiService()
                    .login(company)
                    .enqueue(loginCallback);
        }
    }

    private final Callback<Company> loginCallback = new Callback<Company>() {
        @Override
        public void onResponse(Call<Company> call, Response<Company> response) {
            if (!response.isSuccessful()) {
                Log.i(TAG, response.message());
                System.out.println(response.message());
                Toast.makeText(
                        SignInActivity.this,
                        response.message(),
                        Toast.LENGTH_SHORT).show();
            } else {
                System.out.println(response.body().getToken());
                Company.getInstance()
                        .setToken(response.body().getToken())
                        .setUserRole(response.body().getUserRole());

                Intent intent = new Intent(
                        SignInActivity.this,
                        MenuActivity.class
                );

                startActivity(intent);
            }
        }

        @Override
        public void onFailure(Call<Company> call, Throwable t) {
            Log.i(TAG, t.toString());
            Toast.makeText(
                    SignInActivity.this,
                    t.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    };
}




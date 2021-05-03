package ua.nure.cleaningservice.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Address;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.util.InternetConnection;
import ua.nure.cleaningservice.util.Verification;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    EditText name, email, phoneNumber, password, confirmPassword;
    Button confirmButton;
    Spinner role;
    LinearLayout goToLogin;
    ArrayList<String> roles;

    String companyName;
    int companyId;
    int[] ids;
    List<Company> company;
    Context context;

    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        context = this;

        init();
    }

    private void init() {
        name = findViewById(R.id.name);
        role = findViewById(R.id.get_role_spinner);
        roles = new ArrayList<>();
        roles.add(getString(R.string.placement_owner));
        roles.add(getString(R.string.cleaning_provider));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roles);
        role.setAdapter(adapter);

        name.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Verification.verifyName(this, name);
            }
        });

        email = findViewById(R.id.email);
        email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Verification.verifyEmail(this, email);
            }
        });

        phoneNumber = findViewById(R.id.phone);
        phoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Verification.verifyPhone(this, phoneNumber);
            }
        });

        password = findViewById(R.id.password);
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Verification.verifyPassword(this, password);
            }
        });

        confirmPassword = findViewById(R.id.confirm_password);
        confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Verification.verifyPasswords(this, password, confirmPassword);
            }
        });

        confirmButton = findViewById(R.id.signup_button);
        confirmButton.setOnClickListener(v -> signUp(
                name.getText().toString(),
                email.getText().toString(),
                phoneNumber.getText().toString(),
                password.getText().toString(),
                role.getSelectedItem().toString()
        ));

        goToLogin = findViewById(R.id.go_to_sign_in);
        goToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    //TODO sign up with location
    private void signUp(String name, String email, String phone, String password, String role) {
        if (!Verification.verifyName(this, this.name)
                || !Verification.verifyEmail(this, this.email)
                || !Verification.verifyPhone(this, phoneNumber)
                || !Verification.verifyPassword(this, this.password)
                || !Verification.verifyPasswords(this, this.password, this.confirmPassword)) {
            return;
        } else if (!InternetConnection.checkConnection(getApplicationContext())) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            return;
        } else {
            Company company = Company.getInstance()
                    .setName(name)
                    .setEmail(email)
                    .setPhoneNumber(phone)
                    .setPassword(password)
                    .setUserRole(role)
                    .setAddress(new Address("test", "test", "test", "1", "0", "0"));

            if (role.equals(getString(R.string.placement_owner))) {
                NetworkService.getInstance()
                        .getApiService()
                        .placementOwnerSignUp(company)
                        .enqueue(signUpCallback);
            } else if (role.equals(getString(R.string.cleaning_provider))) {
                NetworkService.getInstance()
                        .getApiService()
                        .cleaningProviderSignUp(company)
                        .enqueue(signUpCallback);
            }
        }
    }

    private final Callback<Company> signUpCallback = new Callback<Company>() {
        @Override
        public void onResponse(Call<Company> call, Response<Company> response) {
            if(!response.isSuccessful()) {
                Log.i(TAG, response.message() + " " + response.code());
                Toast.makeText(
                        SignUpActivity.this,
                        response.message(),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Intent intent = new Intent(SignUpActivity.this,
                        SignInActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onFailure(Call<Company> call, Throwable t) {
            Log.i(TAG, t.getMessage());
            Toast.makeText(
                    SignUpActivity.this,
                    t.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    };

}


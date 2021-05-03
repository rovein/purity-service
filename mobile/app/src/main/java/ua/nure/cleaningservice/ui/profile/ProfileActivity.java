package ua.nure.cleaningservice.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Address;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.auth.SignInActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView mNameTV, mEmailTV, mPhoneTV, mCountryTV, mCityTV, mStreetTV, mHouseTV;
    EditText mNameET, mEmailET, mPhoneET, mCountryET, mCityET, mStreetET, mHouseET, mPasswordET;
    Button mEditButton;

    ImageView mBack, mLogOut;

    Context context;

    private static final String TAG = "ProfileActivity";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        getData();
    }

    private void getData() {
        Callback<Company> callback = new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call,
                    Response<Company> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, response.message());
                    System.out.println(response);
                } else {
                    Company.getInstance().setName(response.body().getName());
                    Company.getInstance()
                            .setPhoneNumber(response.body().getPhoneNumber());
                    Address.getInstance().setCountry(
                            response.body().getAddress().getCountry())
                            .setCity(response.body().getAddress().getCity())
                            .setStreet(response.body().getAddress().getStreet())
                            .setHouseNumber(response.body().getAddress()
                                    .getHouseNumber());

                    mNameTV.setText(Company.getInstance().getName());
                    mPhoneTV.setText(Company.getInstance().getPhoneNumber());
                    mCityTV.setText(Address.getInstance().getCity());
                    mCountryTV.setText(Address.getInstance().getCountry());
                    mStreetTV.setText(Address.getInstance().getStreet());
                    mHouseTV.setText(Address.getInstance().getHouseNumber());
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Log.i(TAG, t.toString());
                System.out.println(t.toString());
            }
        };

        String email = Company.getInstance().getEmail();
        if (Company.getInstance().getUserRole()
                .equals(getString(R.string.cleaning_provider))) {
            NetworkService.getInstance().getApiService()
                    .getCleaningProviderData(token, email).enqueue(callback);
        } else if (Company.getInstance().getUserRole()
                .equals(getString(R.string.placement_owner))) {
            NetworkService.getInstance().getApiService()
                    .getPlacementOwnerData(token, email).enqueue(callback);
        }
    }

    private void init() {
        context = this;
        token = "Bearer " + Company.getInstance().getToken();

        mEmailTV = findViewById(R.id.profile_email);
        mNameTV = findViewById(R.id.profile_name);
        mPhoneTV = findViewById(R.id.profile_phone);
        mCountryTV = findViewById(R.id.profile_country);
        mCityTV = findViewById(R.id.profile_city);
        mStreetTV = findViewById(R.id.profile_street);
        mHouseTV = findViewById(R.id.profile_house);

        mNameET = findViewById(R.id.profile_name_edit);
        mEmailET = findViewById(R.id.profile_email_edit);
        mPhoneET = findViewById(R.id.profile_phone_edit);
        mCountryET = findViewById(R.id.profile_country_edit);
        mCityET = findViewById(R.id.profile_city_edit);
        mStreetET = findViewById(R.id.profile_street_edit);
        mHouseET = findViewById(R.id.profile_house_edit);
        mPasswordET = findViewById(R.id.profile_password_edit);

        mEditButton = findViewById(R.id.edit_profile_btn);
        mEditButton.setOnClickListener(v -> startEditing());

        mBack = findViewById(R.id.back_btn);
        mBack.setOnClickListener(v -> finish());

        mLogOut = findViewById(R.id.exit_btn);
        mLogOut.setOnClickListener(v -> {
            Company.getInstance().setToken(null).setName(null);
            startActivity(
                    new Intent(ProfileActivity.this, SignInActivity.class));
        });

        showTV();
    }

    private void showTV() {
        mEditButton.setText(R.string.edit);

        mNameET.setVisibility(View.GONE);
        mEmailET.setVisibility(View.GONE);
        mPhoneET.setVisibility(View.GONE);
        mCountryET.setVisibility(View.GONE);
        mCityET.setVisibility(View.GONE);
        mStreetET.setVisibility(View.GONE);
        mHouseET.setVisibility(View.GONE);
        mPasswordET.setVisibility(View.GONE);

        mNameTV.setVisibility(View.VISIBLE);
        mEmailTV.setVisibility(View.VISIBLE);
        mPhoneTV.setVisibility(View.VISIBLE);
        mCityTV.setVisibility(View.VISIBLE);
        mCountryTV.setVisibility(View.VISIBLE);
        mStreetTV.setVisibility(View.VISIBLE);
        mHouseTV.setVisibility(View.VISIBLE);

        mNameTV.setText(Company.getInstance().getName());
        mEmailTV.setText(Company.getInstance().getEmail());
    }

    private void showET() {
        mEditButton.setText(R.string.done);

        mNameTV.setVisibility(View.GONE);
        mEmailTV.setVisibility(View.GONE);
        mPhoneTV.setVisibility(View.GONE);
        mCityTV.setVisibility(View.GONE);
        mCountryTV.setVisibility(View.GONE);
        mStreetTV.setVisibility(View.GONE);
        mHouseTV.setVisibility(View.GONE);
        mNameET.setVisibility(View.VISIBLE);
        mEmailET.setVisibility(View.VISIBLE);
        mPhoneET.setVisibility(View.VISIBLE);
        mCountryET.setVisibility(View.VISIBLE);
        mCityET.setVisibility(View.VISIBLE);
        mStreetET.setVisibility(View.VISIBLE);
        mHouseET.setVisibility(View.VISIBLE);
        mPasswordET.setVisibility(View.VISIBLE);

        mNameET.setText(Company.getInstance().getName());
        mEmailET.setText(Company.getInstance().getEmail());
        mPhoneET.setText(Company.getInstance().getPhoneNumber());
        mCityET.setText(Address.getInstance().getCity());
        mCountryET.setText(Address.getInstance().getCountry());
        mStreetET.setText(Address.getInstance().getStreet());
        mHouseET.setText(Address.getInstance().getHouseNumber());
    }

    private void startEditing() {
        showET();
        getData();
        mEditButton.setOnClickListener(v -> submitEditing());
    }

    //TODO edit profile with location
    private void submitEditing() {
        Callback<Company> callback = new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call,
                    Response<Company> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(),
                            Toast.LENGTH_LONG).show();
                } else {
                    mEditButton.setOnClickListener(v -> startEditing());
                    getData();
                    showTV();
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        };

        mNameET.setVisibility(View.GONE);
        mEmailET.setVisibility(View.GONE);
        mPhoneET.setVisibility(View.GONE);
        mCountryET.setVisibility(View.GONE);
        mCityET.setVisibility(View.GONE);
        mStreetET.setVisibility(View.GONE);
        mHouseET.setVisibility(View.GONE);
        mPasswordET.setVisibility(View.GONE);

        Company.getInstance().setName(mNameET.getText().toString())
                .setEmail(mEmailET.getText().toString())
                .setPhoneNumber(mPhoneET.getText().toString()).setAddress(
                new Address(mCountryET.getText().toString(),
                        mCityET.getText().toString(),
                        mStreetET.getText().toString(),
                        mHouseET.getText().toString(), "0", "0"));

        if (Company.getInstance().getUserRole()
                .equals(getString(R.string.cleaning_provider))) {
            NetworkService.getInstance().getApiService()
                    .updateCleaningProviderProfile(token, Company.getInstance())
                    .enqueue(callback);
        } else if (Company.getInstance().getUserRole()
                .equals(getString(R.string.placement_owner))) {
            NetworkService.getInstance().getApiService()
                    .updatePlacementOwnerProfile(token, Company.getInstance())
                    .enqueue(callback);
        }

    }
}
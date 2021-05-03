package ua.nure.cleaningservice.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Service;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.auth.MenuActivity;

public class AddServicesActivity extends AppCompatActivity {

    private static final String TAG = "AddServicesActivity";
    Button mCancelButton, mSaveButton;
    Service mService;
    TextView labelTV;
    EditText mNameET, mRoomTypeET, mDescET, mMinAreaET, mMaxAreaET, mPricePMET;
    private JSONPlaceHolderApi mApi;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        mCancelButton = findViewById(R.id.cancel_add_service_btn);
        mSaveButton = findViewById(R.id.save_add_service_btn);
        labelTV = findViewById(R.id.add_service_label);
        mNameET = findViewById(R.id.add_name_input);
        mRoomTypeET = findViewById(R.id.add_type_input);
        mDescET = findViewById(R.id.add_desc_input);
        mMinAreaET = findViewById(R.id.add_min_area_input);
        mMaxAreaET = findViewById(R.id.add_max_area_input);
        mPricePMET = findViewById(R.id.add_price_pm_input);
        mService = new Service();

        labelTV.setText(R.string.add_service);
        token = "Bearer " + Company.getInstance().getToken();

        mApi = NetworkService.getInstance().getApiService();

        mCancelButton.setOnClickListener(v -> {
            finish();
        });

        mSaveButton.setOnClickListener(v -> {
            addService();
        });
    }

    private void addService() {
        mService.setName(mNameET.getText().toString());
        mService.setPlacementType(mRoomTypeET.getText().toString());
        mService.setDescription(mDescET.getText().toString());
        mService.setMinArea(Integer.parseInt(mMinAreaET.getText().toString()));
        mService.setMaxArea(Integer.parseInt(mMaxAreaET.getText().toString()));
        mService.setPricePerMeter(Float.parseFloat(mPricePMET.getText().toString()));

        String email = Company.getInstance().getEmail();
        mApi.addService(token, email, mService).enqueue(addServiceCallback);
        Intent intent = new Intent(AddServicesActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    Callback<Service> addServiceCallback = new Callback<Service>() {
        @Override
        public void onResponse(Call<Service> call, Response<Service> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                finish();
            }
        }

        @Override
        public void onFailure(Call<Service> call, Throwable t) {
            Log.i(TAG, t.getMessage());
        }
    };
}

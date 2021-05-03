package ua.nure.cleaningservice.ui.edit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Service;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.auth.MenuActivity;

public class EditServicesActivity extends AppCompatActivity {

    private static final String TAG = "EditServicesActivity";
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

        labelTV.setText(R.string.edit_service);
        token = "Bearer " + Company.getInstance().getToken();

        mApi = NetworkService.getInstance().getApiService();

        getService();
        mCancelButton.setOnClickListener(v -> {
            finish();
        });

        mSaveButton.setOnClickListener(v -> {
            saveServices();
        });
    }

    private void getService() {
        mApi.getService(token, getIntent().getIntExtra("sId", -1)).enqueue(serviceCallBack);
    }

    Callback<Service> serviceCallBack = new Callback<Service>() {
        @Override
        public void onResponse(Call<Service> call, Response<Service> response) {
            if (response.isSuccessful()) {
                mNameET.setText(response.body().getName());
                mRoomTypeET.setText(response.body().getPlacementType());
                mDescET.setText(response.body().getDescription());
                mMinAreaET.setText(String.format(Locale.getDefault(), "%d", response.body().getMinArea()));
                mMaxAreaET.setText(String.format(Locale.getDefault(), "%d", response.body().getMaxArea()));
                mPricePMET.setText(String.format(Locale.getDefault(), "%f", response.body().getPricePerMeter()));
                mService = new Service(response.body().getId(), response.body().getName(), response.body().getDescription(), response.body().getMinArea(), response.body().getMaxArea(), response.body().getPlacementType(), response.body().getPricePerMeter());
            }
        }

        @Override
        public void onFailure(Call<Service> call, Throwable t) {
            Log.i(TAG, t.getMessage());
        }
    };

    private void saveServices() {
        mService.setName(mNameET.getText().toString());
        mService.setPlacementType(mRoomTypeET.getText().toString());
        mService.setDescription(mDescET.getText().toString());
        mService.setMinArea(Integer.parseInt(mMinAreaET.getText().toString()));
        mService.setMaxArea(Integer.parseInt(mMaxAreaET.getText().toString()));
        mService.setPricePerMeter(Float.parseFloat(mPricePMET.getText().toString()));

        String email = Company.getInstance().getEmail();
        mApi.updateService(token, email, mService).enqueue(editServiceCallback);
        Intent intent = new Intent(EditServicesActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    Callback<Service> editServiceCallback = new Callback<Service>() {
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

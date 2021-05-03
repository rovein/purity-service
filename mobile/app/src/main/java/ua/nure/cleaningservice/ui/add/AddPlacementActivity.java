package ua.nure.cleaningservice.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Placement;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.auth.MenuActivity;

public class AddPlacementActivity extends AppCompatActivity {

    private static final String TAG = "AddRoomActivity";
    Button mCancelButton, mSaveButton;
    Placement mPlacement;
    TextView labelTV;
    EditText mRoomTypeET, mFloorET, mWinCountET, mAreaET;
    private JSONPlaceHolderApi mApi;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_placement);

        mCancelButton = findViewById(R.id.cancel_add_room_btn);
        mSaveButton = findViewById(R.id.save_add_room_btn);
        labelTV = findViewById(R.id.add_room_label);
        mRoomTypeET = findViewById(R.id.add_type_input);
        mFloorET = findViewById(R.id.add_floor_input);
        mWinCountET = findViewById(R.id.add_win_count_input);
        mAreaET = findViewById(R.id.add_area_input);

        labelTV.setText(R.string.add_room);
        token = "Bearer " + Company.getInstance().getToken();

        mApi = NetworkService.getInstance().getApiService();
        mPlacement = new Placement();

        mCancelButton.setOnClickListener(v -> {
            finish();
        });

        mSaveButton.setOnClickListener(v -> {
            addRoom();
        });
    }

    private void addRoom() {
        mPlacement.setPlacementType(mRoomTypeET.getText().toString());
        mPlacement.setFloor(Integer.parseInt(mFloorET.getText().toString()));
        mPlacement.setWindowsCount(Integer.parseInt(mWinCountET.getText().toString()));
        mPlacement.setArea(Float.parseFloat(mAreaET.getText().toString()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        mPlacement.setLastCleaning(dateFormat.format(new Date()));

        String email = Company.getInstance().getEmail();
        mApi.addPlacement(token, email, mPlacement).enqueue(editRoomCallback);
        Intent intent = new Intent(AddPlacementActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    Callback<Placement> editRoomCallback = new Callback<Placement>() {
        @Override
        public void onResponse(Call<Placement> call, @NotNull Response<Placement> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                finish();
            }
        }

        @Override
        public void onFailure(Call<Placement> call, Throwable t) {
            Log.i(TAG, t.getMessage());
        }
    };
}

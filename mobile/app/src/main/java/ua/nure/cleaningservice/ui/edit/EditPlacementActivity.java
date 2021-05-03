package ua.nure.cleaningservice.ui.edit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Placement;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.auth.MenuActivity;

public class EditPlacementActivity extends AppCompatActivity {

    private static final String TAG = "EditRoomActivity";
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

        labelTV.setText(R.string.edit_room);
        token = "Bearer " + Company.getInstance().getToken();

        mApi = NetworkService.getInstance().getApiService();

        getRoom();
        mCancelButton.setOnClickListener(v -> {
            finish();
        });

        mSaveButton.setOnClickListener(v -> {
            saveRoom();
        });
    }

    private void getRoom() {
        mApi.getPlacement(token, getIntent().getIntExtra("rId", -1)).enqueue(roomCallBack);
    }

    Callback<Placement> roomCallBack = new Callback<Placement>() {
        @Override
        public void onResponse(Call<Placement> call, Response<Placement> response) {
            if (response.isSuccessful()) {
                mRoomTypeET.setText(response.body().getPlacementType());
                mAreaET.setText(String.format(Locale.getDefault(), "%f", response.body().getArea()));
                mFloorET.setText(String.format(Locale.getDefault(), "%d", response.body().getFloor()));
                mWinCountET.setText(String.format(Locale.getDefault(), "%d", response.body().getWindowsCount()));
                mPlacement = new Placement(response.body().getId(), response.body().getPlacementType(), response.body().getFloor(), response.body().getWindowsCount(), response.body().getArea(), response.body().getLastCleaning(), response.body().getSmartDevice());
            }
        }

        @Override
        public void onFailure(Call<Placement> call, Throwable t) {
            Log.i(TAG, t.getMessage());
        }
    };

    private void saveRoom() {
        mPlacement.setPlacementType(mRoomTypeET.getText().toString());
        mPlacement.setFloor(Integer.parseInt(mFloorET.getText().toString()));
        mPlacement.setWindowsCount(Integer.parseInt(mWinCountET.getText().toString()));
        mPlacement.setArea(Float.parseFloat(mAreaET.getText().toString()));

        String email = Company.getInstance().getEmail();
        mApi.updatePlacement(token, email, mPlacement).enqueue(editRoomCallback);
        Intent intent = new Intent(EditPlacementActivity.this, MenuActivity.class);
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

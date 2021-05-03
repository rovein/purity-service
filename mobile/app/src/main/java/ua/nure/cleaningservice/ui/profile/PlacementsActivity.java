package ua.nure.cleaningservice.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Placement;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.add.AddPlacementActivity;
import ua.nure.cleaningservice.ui.auth.MenuActivity;
import ua.nure.cleaningservice.ui.rva.PlacementsRVA;

public class PlacementsActivity extends AppCompatActivity {

    private static final String TAG = "ServicesActivity";

    private List<Placement> mPlacements;
    private RecyclerView mRecyclerView;
    private JSONPlaceHolderApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placements);

        mRecyclerView = findViewById(R.id.rooms_rv);
        ImageButton backButton = findViewById(R.id.back_btn);
        ImageButton addButton = findViewById(R.id.add_room_btn);

        mApi = NetworkService.getInstance().getApiService();

        LinearLayoutManager llm = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        backButton.setOnClickListener((v) -> {
            navigateToScreen(MenuActivity.class);
            finish();
        });

        addButton.setOnClickListener((v) -> {
            navigateToScreen(AddPlacementActivity.class);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeData();
    }

    private void initializeData(){
        mPlacements = new ArrayList<>();
        String email = Company.getInstance().getEmail();
        String token = "Bearer " + Company.getInstance().getToken();
        mApi.getPlacementData(token, email).enqueue(roomCallback);
    }


    Callback<ArrayList<Placement>> roomCallback = new Callback<ArrayList<Placement>>() {
        @Override
        public void onResponse(Call<ArrayList<Placement>> call, Response<ArrayList<Placement>> response) {
            if(!response.isSuccessful()) {
                System.out.println(response.code());
                return;
            }

            ArrayList<Placement> placementList = response.body();
            for (Placement placement : placementList) {
                mPlacements.add(new Placement(placement.getId(), placement.getPlacementType(), placement
                        .getFloor(), placement.getWindowsCount(), placement.getArea(), placement
                        .getLastCleaning(), placement.getSmartDevice()));
            }
            initializeAdapter();
        }

        @Override
        public void onFailure(Call<ArrayList<Placement>> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
        }
    };

    private void initializeAdapter(){
        PlacementsRVA adapter = new PlacementsRVA(this, mPlacements);
        mRecyclerView.setAdapter(adapter);
    }

    private void navigateToScreen(Class cls) {
        Intent intent = new Intent(PlacementsActivity.this,
                cls);
        startActivity(intent);
    }
}
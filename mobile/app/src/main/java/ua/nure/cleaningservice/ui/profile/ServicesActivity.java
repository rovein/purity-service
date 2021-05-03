package ua.nure.cleaningservice.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Service;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.add.AddServicesActivity;
import ua.nure.cleaningservice.ui.auth.MenuActivity;
import ua.nure.cleaningservice.ui.rva.ServicesRVA;

public class ServicesActivity extends AppCompatActivity {

    private static final String TAG = "ServicesActivity";

    private List<Service> mServices;
    private RecyclerView mRecyclerView;
    private JSONPlaceHolderApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        mRecyclerView = (RecyclerView) findViewById(R.id.services_rv);

        ImageButton mBackButton = findViewById(R.id.back_btn);
        ImageButton mAddButton = findViewById(R.id.add_service_btn);

        mApi = NetworkService.getInstance().getApiService();

        LinearLayoutManager llm = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        mBackButton.setOnClickListener((v) -> {
            navigateToScreen(MenuActivity.class);
            finish();
        });

        mAddButton.setOnClickListener((v) -> {
            navigateToScreen(AddServicesActivity.class);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mServices = new ArrayList<>();
        initializeData();
    }

    private void initializeData() {
        String email = Company.getInstance().getEmail();
        String token = "Bearer " + Company.getInstance().getToken();
        mApi.getServiceData(token, email).enqueue(serviceCallback);
    }

    Callback<ArrayList<Service>> serviceCallback = new Callback<ArrayList<Service>>() {
        @Override
        public void onResponse(Call<ArrayList<Service>> call,
                Response<ArrayList<Service>> response) {
            if (!response.isSuccessful()) {
                System.out.println(response.code());
                return;
            }

            ArrayList<Service> serviceList = response.body();
            for (Service service : serviceList) {
                mServices.add(new Service(service.getId(), service.getName(),
                        service.getDescription(), service.getMinArea(),
                        service.getMaxArea(), service.getPlacementType(),
                        service.getPricePerMeter()));
            }
            initializeAdapter();
        }

        @Override
        public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
        }
    };

    private void initializeAdapter() {
        ServicesRVA adapter = new ServicesRVA(this, mServices);
        mRecyclerView.setAdapter(adapter);
    }

    private void navigateToScreen(Class cls) {
        Intent intent = new Intent(ServicesActivity.this, cls);
        startActivity(intent);
    }
}

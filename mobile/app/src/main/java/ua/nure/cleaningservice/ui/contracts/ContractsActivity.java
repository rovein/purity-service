package ua.nure.cleaningservice.ui.contracts;

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
import ua.nure.cleaningservice.data.Contract;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;
import ua.nure.cleaningservice.ui.auth.MenuActivity;
import ua.nure.cleaningservice.ui.rva.ContractsRVA;

public class ContractsActivity extends AppCompatActivity {

    private static final String TAG = "ContractsActivity";

    private List<Contract> mContracts;
    private RecyclerView mRecyclerView;
    private JSONPlaceHolderApi mApi;
    private ImageButton mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);

        mRecyclerView = findViewById(R.id.contracts_rv);
        mBackButton = findViewById(R.id.back_btn);

        mApi = NetworkService.getInstance().getApiService();

        LinearLayoutManager llm = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        mBackButton.setOnClickListener((v) -> {
            navigateToMenuScreen();
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeData();
    }

    private void initializeData() {
        mContracts = new ArrayList<>();
        Company company = Company.getInstance();
        String email = company.getEmail();
        String token = "Bearer " + company.getToken();
        String role = company.getUserRole();

        if (role.equals(getString(R.string.cleaning_provider))) {
            mApi.getCleaningProviderContracts(token, email).enqueue(contractsCallback);
        } else if (role.equals(getString(R.string.placement_owner))) {
            mApi.getPlacementOwnerContracts(token, email).enqueue(contractsCallback);
        }
    }

    Callback<ArrayList<Contract>> contractsCallback = new Callback<ArrayList<Contract>>() {

        @Override
        public void onResponse(Call<ArrayList<Contract>> call,
                Response<ArrayList<Contract>> response) {
            if (!response.isSuccessful()) {
                System.out.println(response.code());
                return;
            }
            List<Contract> contracts = response.body();
            for (Contract contract : contracts) {
                mContracts.add(new Contract(contract.getDate(),
                        contract.getPrice(), contract.getServiceName(),
                        contract.getPlacementId(), contract.getProviderServiceId(),
                        contract.getId(), contract.getCleaningProviderName(),
                        contract.getPlacementOwnerName()));
            }
            initializeAdapter();
        }

        @Override
        public void onFailure(Call<ArrayList<Contract>> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
        }
    };

    private void initializeAdapter() {
        ContractsRVA adapter = new ContractsRVA(this, mContracts);
        mRecyclerView.setAdapter(adapter);
    }

    private void navigateToMenuScreen() {
        Intent intent = new Intent(ContractsActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
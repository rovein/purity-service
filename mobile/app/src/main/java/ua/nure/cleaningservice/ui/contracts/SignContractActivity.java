package ua.nure.cleaningservice.ui.contracts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Contract;
import ua.nure.cleaningservice.data.Placement;
import ua.nure.cleaningservice.data.Service;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;

public class SignContractActivity extends AppCompatActivity {

    private static final String TAG = "SignContractActivity";

    private List<Contract> mContracts;
    private JSONPlaceHolderApi mApi;
    ImageView mBack;
    Button mSignContractButton;
    Spinner mServicesSpinner, mRoomsSpinner;
    Contract mContract;

    int roomId;
    int serviceId;
    int[] servicesId;
    List<Service> mServices;
    List<Placement> mPlacements;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_contract_activity);

        mBack = findViewById(R.id.back_btn);
        mSignContractButton = findViewById(R.id.sign_contract_btn);
        mSignContractButton.setOnClickListener(v -> signContract());

        mApi = NetworkService.getInstance().getApiService();
        mContract = new Contract();

        mServicesSpinner = findViewById(R.id.get_service_spinner);
        mServicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serviceId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRoomsSpinner = findViewById(R.id.get_room_spinner);
        mRoomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roomId = mPlacements.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    Callback<ArrayList<Service>> serviceCallback = new Callback<ArrayList<Service>>() {
        @Override
        public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
            if (!response.isSuccessful()) {
                System.out.println(response.code());
                return;
            }
            mServices = response.body();
            String[] names = new String[mServices.size()];
            servicesId = new int[mServices.size()];
            int i = 0;
            for (Service service : mServices) {
                names[i] = service.getName();
                servicesId[i] = service.getId();
                i++;
            }
            initServicesAdapter(names);
        }

        @Override
        public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
        }
    };

    Callback<ArrayList<Placement>> roomsCallback = new Callback<ArrayList<Placement>>() {
        @Override
        public void onResponse(Call<ArrayList<Placement>> call, Response<ArrayList<Placement>> response) {
            if (!response.isSuccessful()) {
                System.out.println(response.code());
                return;
            }
            mPlacements = response.body();
            Integer[] ids = new Integer[mPlacements.size()];
            int i = 0;
            for (Placement placement : mPlacements) {
                ids[i] = placement.getId();
                i++;
            }
            initRoomsAdapter(ids);
        }

        @Override
        public void onFailure(Call<ArrayList<Placement>> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
        }
    };

    Callback<Contract> signContractCallback = new Callback<Contract>() {
        @Override
        public void onResponse(Call<Contract> call, Response<Contract> response) {
            if (!response.isSuccessful()) {
                System.out.println(response.code());
                return;
            }
            finish();
        }

        @Override
        public void onFailure(Call<Contract> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
        }
    };

    private void getData() {
        String email = Company.getInstance().getEmail();
        token = "Bearer " + Company.getInstance().getToken();

        mApi.getServiceData(token, getIntent().getStringExtra("cEmail")).enqueue(serviceCallback);
        mApi.getPlacementData(token, email).enqueue(roomsCallback);
    }

    private void initServicesAdapter(String[] servicesData){
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, servicesData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mServicesSpinner.setAdapter(adapter);
    }

    private void initRoomsAdapter(Integer[] roomsData){
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, roomsData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRoomsSpinner.setAdapter(adapter);
    }

    private void signContract() {
        mContract.setProviderServiceId(servicesId[serviceId]);
        mContract.setPlacementId(roomId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        mContract.setDate(dateFormat.format(new Date()));

            NetworkService.getInstance()
                    .getApiService()
                    .signContract(token, mContract)
                    .enqueue(signContractCallback);

    }

}
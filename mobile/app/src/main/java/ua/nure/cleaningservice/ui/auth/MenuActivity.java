package ua.nure.cleaningservice.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.ui.contracts.ContractsActivity;
import ua.nure.cleaningservice.ui.profile.MapActivity;
import ua.nure.cleaningservice.ui.profile.ProfileActivity;
import ua.nure.cleaningservice.ui.profile.PlacementsActivity;
import ua.nure.cleaningservice.ui.profile.SearchActivity;
import ua.nure.cleaningservice.ui.profile.ServicesActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Company.getInstance().getUserRole().equals(getString(R.string.cleaning_provider))){
            setContentView(R.layout.activity_cleaning_provider_menu);
        }else if(Company.getInstance().getUserRole().equals(getString(R.string.placement_owner))){
            setContentView(R.layout.activity_placement_owner_menu);
        }
        init();
    }

    private void init() {
        if(Company.getInstance().getUserRole().equals(getString(R.string.cleaning_provider))){
            findViewById(R.id.services_btn).setOnClickListener(v -> goTo(
                    ServicesActivity.class));
        }else if(Company.getInstance().getUserRole().equals(getString(R.string.placement_owner))){
            findViewById(R.id.rooms_btn).setOnClickListener(v -> goTo(
                    PlacementsActivity.class));
            findViewById(R.id.search_btn).setOnClickListener(v -> goTo(
                    SearchActivity.class));
            findViewById(R.id.map_btn).setOnClickListener(v -> goTo(MapActivity.class));
        }
        findViewById(R.id.profile_btn).setOnClickListener(v -> goTo(
                ProfileActivity.class));
        findViewById(R.id.contracts_btn).setOnClickListener(v -> goTo(
                ContractsActivity.class));
    }

    private void goTo(Class cls) {
        Intent intent = new Intent(MenuActivity.this, cls);
        startActivity(intent);
    }
}
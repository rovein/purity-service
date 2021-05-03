package ua.nure.cleaningservice.ui.profile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import ua.nure.cleaningservice.R;
import ua.nure.cleaningservice.network.JSONPlaceHolderApi;
import ua.nure.cleaningservice.network.NetworkService;

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private GoogleMap mMap;
    private MapMarkerIcon mMapMarkerIcon;
    private JSONPlaceHolderApi mApi;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapMarkerIcon = new MapMarkerIcon();

        mApi = NetworkService.getInstance().getApiService();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        mapView = findViewById(R.id.map);
    }
    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_CODE);
            return;
        }

        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if(location != null) {
                mCurrentLocation = location;
                System.out.println(mCurrentLocation);
                mapView.getMapAsync(googleMap -> {
                    mMap = googleMap;
                    showLocation(location);
                });
            }
        });
    }

    private void showLocation (Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        BitmapDescriptor icon = mMapMarkerIcon.getCurrentLocationIcon(this);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(getString(R.string.current_location))
                .icon(icon);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        mMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        switch(requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    private class MapMarkerIcon {

        private BitmapDescriptor currentLocationIcon;

        public BitmapDescriptor getCurrentLocationIcon(Context context) {
            if(currentLocationIcon == null) { createCurrentLocationIcon(context); }
            return currentLocationIcon;
        }

        private void createCurrentLocationIcon(Context context) {
            Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_my_location_background);
            background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
            Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_my_location);
            vectorDrawable.setBounds(18, 18, vectorDrawable.getIntrinsicWidth() + 18, vectorDrawable.getIntrinsicHeight() + 18);
            Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            background.draw(canvas);
            vectorDrawable.draw(canvas);
            currentLocationIcon = BitmapDescriptorFactory.fromBitmap(bitmap);
        }
    }
}
package com.example.toys_exchange.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.toys_exchange.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient mFusedLocationClient;

    private static final String TAG = MapActivity.class.getSimpleName();
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    // public EditText search;

    public SearchView search;

    Double longitude;
    Double latitude;

    Double searchedLongitude;
    Double searchedLatitude;

    TextView add;
    TextView close;

    private final int PERMISSION_ID = 44;


    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            LatLng coordinate = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(coordinate)
                    .title(" location "));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 20f));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        add=findViewById(R.id.btn_add);
        close=findViewById(R.id.btn_close);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        enableLocation();
//
//        search=findViewById(R.id.sv_location);
        search=findViewById(R.id.sv_location);
        supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String txt=search.getQuery().toString();
                Geocoder geocoder=new Geocoder(MapActivity.this);
                List<Address> list=new ArrayList<>();
                try {

                    list=geocoder.getFromLocationName(txt,1);

                }catch (IOException e){
                    Log.i(TAG, "geoLocate: "+e.getMessage());
                }
                if(list.size()>0){
                    Address address=list.get(0);

                    searchedLatitude=address.getLatitude();
                    searchedLongitude=address.getLongitude();
                    LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(txt));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

                    Log.i(TAG, "geoLocate: --------------------------->"+address);



                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        supportMapFragment.getMapAsync(this);

        close.setOnClickListener(view -> {

            Intent intent=getIntent();
            String typeActivity=intent.getStringExtra("type");
            if(typeActivity.equals("store")) {
                startActivity(new Intent(getApplicationContext(), StoreAddActivity.class));
                Toast.makeText(getApplicationContext(), "closed ", Toast.LENGTH_SHORT).show();
            }else {
                startActivity(new Intent(getApplicationContext(), EventActivity.class));
                Toast.makeText(getApplicationContext(), "closed ", Toast.LENGTH_SHORT).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title;
                String desc;
                Intent intent=getIntent();
                String typeActivity=intent.getStringExtra("type");

                if(typeActivity.equals("store")){
                     title=intent.getStringExtra("title");
                     desc=intent.getStringExtra("desc");
                    Intent locationIntent=new Intent(getApplicationContext(),StoreAddActivity.class);
                    locationIntent.putExtra("longitude",searchedLongitude);
                    locationIntent.putExtra("latitude",searchedLatitude);
                    locationIntent.putExtra("title",title);
                    locationIntent.putExtra("desc",desc);
                    startActivity(locationIntent);
                    Toast.makeText(getApplicationContext(), "location added ", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onClick: store lang------------------------------->"+ searchedLongitude);
                    Log.i(TAG, "onClick: store lat------------------------------->"+ searchedLatitude);
                }else if(typeActivity.equals("event")){
                     title=intent.getStringExtra("title");
                     desc=intent.getStringExtra("desc");
                    Intent locationIntent=new Intent(getApplicationContext(),EventActivity.class);
                    locationIntent.putExtra("longitude",searchedLongitude);
                    locationIntent.putExtra("latitude",searchedLatitude);
                    locationIntent.putExtra("title",title);
                    locationIntent.putExtra("desc",desc);
                    startActivity(locationIntent);
                    Toast.makeText(getApplicationContext(), "location added ", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onClick: event lang------------------------------->"+ searchedLongitude);
                    Log.i(TAG, "onClick: event lat------------------------------->"+ searchedLatitude);
                    Log.i(TAG, "onClick: event title------------------------------->"+ title);
                    Log.i(TAG, "onClick: event desc------------------------------->"+ desc);
                }

            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;
    }



    ///////////////////////////////////////////////////// Location //////////////////////////////////////////////
    @SuppressLint("MissingPermission")
    private void enableLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            LatLng coordinate = new LatLng(latitude, longitude);



//                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 20.0f));
                        }
                    }
                });
                Toast.makeText(this, "Your Location Enabled... ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }


    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            enableLocation();
        }
    }
}
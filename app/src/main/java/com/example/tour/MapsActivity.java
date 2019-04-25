package com.example.tour;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.tour.NearbyLocation.NearbyLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

   /* private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/

    private GoogleMap mMap;
    private Geocoder geocoder;
    List<Address> addresses;
    FragmentManager fragmentManager;
    AutoCompleteTextView autoCompleteTextView;
    FusedLocationProviderClient fusedLocationProviderClient;
    private NearbyLocation nearbyLocation;
    private List list;
    private double lat, lng;
    String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        autoCompleteTextView = findViewById(R.id.autoSearchViewSV);
        geocoder = new Geocoder(this);

        getLocationPermission();


    }

    private void initializeMap() {
        fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.map, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeMap();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng dhaka = new LatLng(23.7527022, 90.3927751);
        //mMap.addMarker(new MarkerOptions().position(dhaka).title("Marker in Dhaka"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dhaka, 14));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().isMyLocationButtonEnabled();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(latLng.latitude + "," + latLng.longitude)).snippet(addresses.get(0).getLocality() + "," + addresses.get(0).getCountryCode()));
            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    autoCompleteTextView.setText(addresses.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void myCurrentLocation(View view) {
        getMyLocation();
    }

    private void getMyLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()){
                    Location location = task.getResult();
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    placeName = getIntent().getStringExtra("pName");
                    getNearbyLocationData(lat,lng,placeName);
                    LatLng testLocation = new LatLng(lat,lng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(testLocation,14));

                }
            }
        });
    }

    private void getNearbyLocationData(double nLat, double nLng, String pName) {

        Location_Interface location_interface = RetrofitClass.getRetrofitLocationInstance().create(Location_Interface.class);
        String url = String.format("json?location=%f,%f&radius=1500&type=%s&key=AIzaSyAGKJfnsFM5lV-LP0KTw5RhjlRbNqwi2Pc", nLat, nLng, pName);
        final Call<NearbyLocation> nearbyLocationCall = location_interface.getLocationData(url);

        nearbyLocationCall.enqueue(new Callback<NearbyLocation>() {
            @Override
            public void onResponse(Call<NearbyLocation> call, Response<NearbyLocation> response) {

                if (response.code() == 200) {
                    nearbyLocation = response.body();

                    list = new ArrayList();
                    list = nearbyLocation.getResults();
                    for (int i = 0; i < list.size(); i++) {
                        double nearbyLat = nearbyLocation.getResults().get(i).getGeometry().getLocation().getLat();
                        double nearbyLng = nearbyLocation.getResults().get(i).getGeometry().getLocation().getLng();
                        LatLng nearbyLocation = new LatLng(nearbyLat, nearbyLng);
                        mMap.addMarker(new MarkerOptions().position(nearbyLocation));
                    }
                }

            }

            @Override
            public void onFailure(Call<NearbyLocation> call, Throwable t) {

                Toast.makeText(MapsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
/*
    public void showBank(View view) {

        placeName = "bank";

        getMyLocation();

    }

    public void showHospital(View view) {

        placeName = "hospital";
        getMyLocation();

    }

    public void showSchool(View view) {

        placeName = "school";
        getMyLocation();

    }*/
}

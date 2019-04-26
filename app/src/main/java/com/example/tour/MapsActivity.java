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

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private List<Address> addresses;
    private FragmentManager fragmentManager;
    private AutoCompleteTextView autoCompleteTextView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private NearbyLocation nearbyLocation;
    private List list;
    private double lat, lng;
    private String placeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoSearchViewSV);
        geocoder = new Geocoder(this);
        getLocationPermission();
        /*// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeMap();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    private void initializeMap() {
        fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.map, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
                mMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(latLng.latitude) + " , " + String.valueOf(latLng.longitude))
                        .snippet(addresses.get(0).getLocality() + " , " + addresses.get(0).getCountryCode()));
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

    public void getMyLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()){
                    Location location = task.getResult();
                    lat=location.getLatitude();
                    lng=location.getLongitude();
                    placeName=getIntent().getStringExtra("pName");
                    getNearbyLocationData(lat,lng,placeName);
                    LatLng testLocation = new LatLng(lat,lng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(testLocation, 14));
                }
            }
        });

    }

    private void getNearbyLocationData(double nlat, double nlng, String pName) {
        Location_Interface location_interface = RetrofitClass.getRetrofitLocationInstance().create(Location_Interface.class);
        String url = String.format("json?location=%f,%f&radius=1500&type=%s&key=AIzaSyCw_D_bCK_GqSZ3Z4Re7Hd7VqaT7flU0Wc", nlat, nlng, pName);
        Call<NearbyLocation> nearbyLocationCall = location_interface.getLocationData(url);
        nearbyLocationCall.enqueue(new Callback<NearbyLocation>() {
            @Override
            public void onResponse(Call<NearbyLocation> call, Response<NearbyLocation> response) {
                if (response.code()==200){
                    nearbyLocation=response.body();
                    list = new ArrayList();
                    list=nearbyLocation.getResults();
                    for (int i =0;i<list.size();i++){
                        double nearbyLat = nearbyLocation.getResults().get(i).getGeometry().getLocation().getLat();
                        double nearbyLng = nearbyLocation.getResults().get(i).getGeometry().getLocation().getLng();
                        LatLng nearbyLocation = new LatLng(nearbyLat,nearbyLng);
                        mMap.addMarker(new MarkerOptions().position(nearbyLocation));
                    }
                }
            }

            @Override
            public void onFailure(Call<NearbyLocation> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();

        CustomIntent.customType(MapsActivity.this,"right-to-left");
        finish();
    }
}

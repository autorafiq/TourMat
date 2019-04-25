package com.example.tour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NearbyPlaceList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_place_list);

    }

    public void goToAtm(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","atm"));
    }

    public void goToSchool(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","school"));
    }

    public void goToTrain(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","train"));
    }

    public void goToPolice(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","police"));
    }

    public void goToMosque(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","mosque"));
    }

    public void goToHospital(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","hospital"));
    }

    public void goToAirport(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","airport"));
    }

    public void goToCafe(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","cafe"));
    }

    public void goToRestaurant(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","restaurant"));
    }

    public void goToBank(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","bank"));
    }
}

package com.example.tour;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import maes.tech.intentanim.CustomIntent;

public class NearbyPlaceList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_place_list);

    }

    public void goToAtm(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","atm"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToSchool(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","school"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToTrain(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","train"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToPolice(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","police"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToMosque(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","mosque"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToHospital(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","hospital"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToAirport(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","airport"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToCafe(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","cafe"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToRestaurant(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","restaurant"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goToBank(View view) {
        startActivity(new Intent(this,MapsActivity.class).putExtra("pName","bank"));
        CustomIntent.customType(NearbyPlaceList.this,"left-to-right");
    }

    public void goBack(View view) {
        onBackPressed();

        CustomIntent.customType(NearbyPlaceList.this,"right-to-left");
        finish();
    }


}

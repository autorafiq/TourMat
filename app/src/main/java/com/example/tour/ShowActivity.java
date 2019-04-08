package com.example.tour;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tour.Data.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private TourRecyclerView tourRecyclerViewAdapter;
    private ArrayList<Data> dataList;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        recyclerView = findViewById(R.id.recyclerView);
        dataList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser().getUid();
        userRef = database.getReference("tourUser").child(user).child("event");
        //replaceFragment(new TourListFragment());

        getDataFromDatabase();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tourRecyclerViewAdapter = new TourRecyclerView(dataList, getApplicationContext());
        recyclerView.setAdapter(tourRecyclerViewAdapter);
        getFlottingButton();
    }

    private void getDataFromDatabase() {
         userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Data allData = data.getValue(Data.class);
                        dataList.add(allData);
                        tourRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowActivity.this, "Can not read from DataBase.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private void getFlottingButton() {
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_add_black_24dp);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:

        ImageView detailsIcon = new ImageView(this);
        detailsIcon.setImageResource(R.drawable.ic_details_black_24dp);
        SubActionButton button1 = itemBuilder.setContentView(detailsIcon).build();

        ImageView memoriesIcon = new ImageView(this);
        memoriesIcon.setImageResource(R.drawable.ic_center_focus_weak_black_24dp);
        SubActionButton button2 = itemBuilder.setContentView(memoriesIcon).build();

        ImageView moneyIcon = new ImageView(this);
        moneyIcon.setImageResource(R.drawable.ic_attach_money_black_24dp);
        SubActionButton button3 = itemBuilder.setContentView(moneyIcon).build();

        ImageView logoutIcon = new ImageView(this);
        logoutIcon.setImageResource(R.drawable.ic_call_missed_outgoing_black_24dp);
        SubActionButton button4 = itemBuilder.setContentView(logoutIcon).build();


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                // ...
                .attachTo(actionButton)
                .build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowActivity.this, AddTourActivity.class)); //Go back to home page
                finish();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MemoriesFragment());
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MoneyFragment());
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //End user session
                startActivity(new Intent(ShowActivity.this, SingInActivity.class)); //Go back to home page
                finish();
            }
        });

    }
}

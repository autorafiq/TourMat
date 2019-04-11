package com.example.tour;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.tour.Data.Image;
import com.example.tour.databinding.ActivityImageShowBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageShowActivity extends AppCompatActivity {
    ActivityImageShowBinding binding;
    private ImageAdapter imageAdapter;
    private List<Image> imageList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_show);
        mAuth = FirebaseAuth.getInstance();
        binding.imageRecyclerView.setHasFixedSize(true);
        binding.imageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageList = new ArrayList<>();
        String userId = mAuth.getCurrentUser().getUid();
        eventId = getIntent().getStringExtra("eventId");
        databaseReference = FirebaseDatabase.getInstance().getReference("tourUser").child(userId).child("event").child(eventId).child("memories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot loadedData : dataSnapshot.getChildren()) {
                        Image imageData = loadedData.getValue(Image.class);
                        imageList.add(imageData);
                    }
                    imageAdapter=new ImageAdapter(ImageShowActivity.this, imageList);
                    binding.imageRecyclerView.setAdapter(imageAdapter);
                }
                binding.imageViewPB.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ImageShowActivity.this, "Error :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                binding.imageViewPB.setVisibility(View.INVISIBLE);
            }
        });
    }
}

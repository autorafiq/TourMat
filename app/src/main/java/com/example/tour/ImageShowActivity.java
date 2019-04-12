package com.example.tour;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.tour.Data.Image;
import com.example.tour.databinding.ActivityImageShowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageShowActivity extends AppCompatActivity {
    ActivityImageShowBinding binding;
    private ImageAdapter imageAdapter;
    private List<Image> imageList;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_show);
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
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
                    imageList.clear();
                    for (DataSnapshot loadedData : dataSnapshot.getChildren()) {
                        Image imageData = loadedData.getValue(Image.class);
                        imageList.add(imageData);
                    }
                    imageAdapter = new ImageAdapter(ImageShowActivity.this, imageList);
                    binding.imageRecyclerView.setAdapter(imageAdapter);
                    imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(int position) {
                            String text = imageList.get(position).getImageCaption();
                            Toast.makeText(ImageShowActivity.this, text + " is selected" + position, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnEdit(int position) {
                            Image selectEditItem = imageList.get(position);
                            final String editKey = selectEditItem.getImageId();
                            Intent editIntent = new Intent(getApplicationContext(), UpdateMemoriesActivity.class);
                            editIntent.putExtra("eventId", eventId);
                            editIntent.putExtra("memoriesId",editKey);
                            editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(editIntent);
                            //Toast.makeText(ImageShowActivity.this, "Edit is selected" + position, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnDelete(int position) {
                            Image selectItem = imageList.get(position);
                            final String key = selectItem.getImageId();
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(selectItem.getImageUri());
                            storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        databaseReference.child(key).removeValue();
                                        Toast.makeText(ImageShowActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //Toast.makeText(ImageShowActivity.this, "Delete is selected" + key, Toast.LENGTH_SHORT).show();
                        }
                    });
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

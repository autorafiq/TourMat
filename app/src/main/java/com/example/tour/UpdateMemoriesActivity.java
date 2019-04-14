package com.example.tour;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.tour.Data.Image;
import com.example.tour.databinding.ActivityUpdateExpenseBinding;
import com.example.tour.databinding.ActivityUpdateMemoriesBinding;
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
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class UpdateMemoriesActivity extends AppCompatActivity {
    ActivityUpdateMemoriesBinding binding;
    private String userId, eventId, memoriesId;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private static final int IMAGE_REQUEST = 100;
    private String imageName;

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(UpdateMemoriesActivity.this,"right-to-left");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_memories);
        mAuth = FirebaseAuth.getInstance();


        userId = mAuth.getCurrentUser().getUid();
        eventId = getIntent().getStringExtra("eventId");
        memoriesId = getIntent().getStringExtra("memoriesId");
        //Toast.makeText(getApplicationContext(), "" + eventId + " / " + memoriesId, Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference("tourUser").child(userId).child("event").child(eventId).child("memories").child(memoriesId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    binding.updateEditCaptionET.setText(dataSnapshot.child("imageCaption").getValue().toString());
                    Picasso.with(getApplicationContext()).load(dataSnapshot.child("imageUri").getValue().toString())
                            .placeholder(R.mipmap.ic_launcher_round)
                            .fit().centerCrop().into(binding.updateImageIV);
                }

                binding.updateImageViewPB.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateMemoriesActivity.this, "Error : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                binding.updateImageViewPB.setVisibility(View.INVISIBLE);
            }
        });

        binding.updateShowMemoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageShowActivity.class);
                intent.putExtra("eventId", eventId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                CustomIntent.customType(UpdateMemoriesActivity.this,"left-to-right");
            }
        });
        binding.updateImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openImageFile();
                Toast.makeText(getApplicationContext(), "You can not replace image.", Toast.LENGTH_LONG).show();
            }
        });
        binding.updateAddMemoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = binding.updateEditCaptionET.getText().toString();

                if (imageName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your image name", Toast.LENGTH_SHORT).show();
                } else {

                    addImageCaption(new Image(imageName));
                }

            }
        });
    }

       private void addImageCaption(Image image) {

        databaseReference.child("imageCaption").setValue(imageName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Caption Updated", Toast.LENGTH_SHORT).show();

                    CustomIntent.customType(UpdateMemoriesActivity.this,"right-to-left");
                    finish();
                }
            }
        });
    }

}

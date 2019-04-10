package com.example.tour;

import android.content.ContentResolver;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.tour.Data.Data;
import com.example.tour.Data.Image;
import com.example.tour.databinding.ActivityAddMemoriesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddMemoriesActivity extends AppCompatActivity {
    ActivityAddMemoriesBinding binding;
    private static final int IMAGE_REQUEST = 1;
    private Uri uri;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<Data> dataList;
    private String eventId;
    private StorageTask storageTask;
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList<>();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_memories);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.imageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFile();
            }
        });
        binding.addMemoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageName = binding.editCaptionET.getText().toString().trim();

                if (storageTask != null && storageTask.isInProgress()) {
                    Toast.makeText(AddMemoriesActivity.this, "Uploading in progress...", Toast.LENGTH_SHORT).show();
                } else if (uri == null) {
                    Toast.makeText(AddMemoriesActivity.this, "Please select your image", Toast.LENGTH_SHORT).show();
                } else if (imageName.isEmpty()) {
                    Toast.makeText(AddMemoriesActivity.this, "Please enter your image name", Toast.LENGTH_SHORT).show();
                } else {
                    addImage();
                }


            }
        });
    }


    private void openImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.with(this).load(uri).into(binding.imageIV);
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void addImage() {

        storageReference = storage.getReference("event");
        StorageReference refStore = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        refStore.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot != null) {
                    Toast.makeText(AddMemoriesActivity.this, "Image stored successfully", Toast.LENGTH_SHORT).show();
                    String userId = mAuth.getCurrentUser().getUid();
                    String eventId = getIntent().getStringExtra("tourUid");
                    myRef = database.getReference("tourUser").child(userId).child("event").child(eventId);
                    Image imageData = new Image(imageName, taskSnapshot.getStorage().getDownloadUrl().toString());
                    String memoriesId = myRef.push().getKey();
                    imageData.setImageId(memoriesId);
                    myRef.child("memories").child(memoriesId).setValue(imageData);
                } else {
                    Toast.makeText(AddMemoriesActivity.this, "Image not stored successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

package com.example.tour.Data;

import android.net.Uri;

public class Image {
    private String imageCaption;
    private String imageUri;
    private String imageId;

    public Image() {
    }

    public Image(String imageCaption, String imageUri) {
        this.imageCaption = imageCaption;
        this.imageUri = imageUri;
    }

    public String getImageCaption() {
        return imageCaption;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}

package com.example.pablo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageExam {
    @SerializedName("image")
    @Expose
    private String image;

    public ImageExam(String image) {
        this.image = image;
    }

    public ImageExam() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

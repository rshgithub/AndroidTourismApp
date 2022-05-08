package com.example.pablo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantsExam {
    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("resName")
    @Expose
    private String resName;

    @SerializedName("foodName")
    @Expose
    private String foodName;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("details")
    @Expose
    private String details;

    @SerializedName("location")
    @Expose
    private String location;

    public RestaurantsExam() {
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

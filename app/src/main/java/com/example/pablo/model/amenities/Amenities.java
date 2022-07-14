
package com.example.pablo.model.amenities;

import com.example.pablo.model.churchesdetails.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Amenities implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<AmenitiesData> data;
    private final static long serialVersionUID = 260295002170889547L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AmenitiesData> getData() {
        return data;
    }

    public void setData(List<AmenitiesData> data) {
        this.data = data;
    }

}

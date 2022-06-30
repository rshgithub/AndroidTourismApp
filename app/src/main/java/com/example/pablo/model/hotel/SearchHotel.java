
package com.example.pablo.model.hotel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchHotel implements Serializable {


    @SerializedName("data")
    @Expose
    private List<HotelsData> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<HotelsData> getData() {
        return data;
    }

    public void setData(List<HotelsData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
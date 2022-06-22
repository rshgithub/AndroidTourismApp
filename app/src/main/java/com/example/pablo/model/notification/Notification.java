
package com.example.pablo.model.notification;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<NotificationData> data = null;
    private final static long serialVersionUID = 5807021806896011436L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationData> getData() {
        return data;
    }

    public void setData(List<NotificationData> data) {
        this.data = data;
    }

}

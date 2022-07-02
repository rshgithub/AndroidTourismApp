
package com.example.pablo.model.notification;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationData implements Serializable
{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("hotel_order_id")
    @Expose
    private Long hotelOrderId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("hotel_name")
    @Expose
    private String hotelName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("created_from")
    @Expose
    private String time_count;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    private final static long serialVersionUID = -8540597643739069433L;

    public String getTime_count() {
        return time_count;
    }

    public void setTime_count(String time_count) {
        this.time_count = time_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelOrderId() {
        return hotelOrderId;
    }

    public void setHotelOrderId(Long hotelOrderId) {
        this.hotelOrderId = hotelOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}

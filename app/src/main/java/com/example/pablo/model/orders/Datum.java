
package com.example.pablo.model.orders;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable
{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("user_id")
    @Expose
    private Long userId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("time_count")
    @Expose
    private String timeCount;
    @SerializedName("hotel_name")
    @Expose
    private String hotelName;
    @SerializedName("total_price")
    @Expose
    private Long totalPrice;
    @SerializedName("order_items_count")
    @Expose
    private Long orderItemsCount;
    private final static long serialVersionUID = -7116576093380600927L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(String timeCount) {
        this.timeCount = timeCount;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getOrderItemsCount() {
        return orderItemsCount;
    }

    public void setOrderItemsCount(Long orderItemsCount) {
        this.orderItemsCount = orderItemsCount;
    }

}
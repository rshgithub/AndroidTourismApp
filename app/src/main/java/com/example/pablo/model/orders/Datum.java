
package com.example.pablo.model.orders;

import java.io.Serializable;
import java.util.List;
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
    private Long status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("total_price")
    @Expose
    private Long totalPrice;
    @SerializedName("order_items_count")
    @Expose
    private Long orderItemsCount;
    @SerializedName("hotel_order_items")
    @Expose
    private List<HotelOrderItem> hotelOrderItems = null;
    private final static long serialVersionUID = 7071437592702857026L;

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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public List<HotelOrderItem> getHotelOrderItems() {
        return hotelOrderItems;
    }

    public void setHotelOrderItems(List<HotelOrderItem> hotelOrderItems) {
        this.hotelOrderItems = hotelOrderItems;
    }

}

package com.example.pablo.model.orders;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("total_price")
    @Expose
    private Integer totalPrice;
    @SerializedName("order_items_count")
    @Expose
    private Integer orderItemsCount;
    @SerializedName("hotel_order_items")
    @Expose
    private List<HotelOrderItem> hotelOrderItems = null;
    private final static long serialVersionUID = 591415264468376928L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getOrderItemsCount() {
        return orderItemsCount;
    }

    public void setOrderItemsCount(Integer orderItemsCount) {
        this.orderItemsCount = orderItemsCount;
    }

    public List<HotelOrderItem> getHotelOrderItems() {
        return hotelOrderItems;
    }

    public void setHotelOrderItems(List<HotelOrderItem> hotelOrderItems) {
        this.hotelOrderItems = hotelOrderItems;
    }

}

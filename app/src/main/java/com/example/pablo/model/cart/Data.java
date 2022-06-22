
package com.example.pablo.model.cart;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data extends CartExample implements Serializable{

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
        private int totalPrice;
        @SerializedName("order_items_count")
        @Expose
        private Long orderItemsCount;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        private final static long serialVersionUID = -1539162111715642534L;

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

        public int getTotalPrice() {
        return totalPrice;
    }

        public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

        public Long getOrderItemsCount() {
        return orderItemsCount;
    }

        public void setOrderItemsCount(Long orderItemsCount) {
        this.orderItemsCount = orderItemsCount;
    }

        public String getCreatedAt() {
        return createdAt;
    }

        public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    }
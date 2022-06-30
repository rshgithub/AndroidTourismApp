
package com.example.pablo.model.bookingInfo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("room_count")
    @Expose
    private Integer roomCount;
    @SerializedName("total_nights")
    @Expose
    private Integer totalNights;
    @SerializedName("room_price_per_night")
    @Expose
    private Integer roomPricePerNight;
    @SerializedName("room_has_offer")
    @Expose
    private Integer roomHasOffer;
    @SerializedName("savings_per_room")
    @Expose
    private Integer savingsPerRoom;
    @SerializedName("order_total_price")
    @Expose
    private Integer orderTotalPrice;
    @SerializedName("time_count")
    @Expose
    private String timeCount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    private final static long serialVersionUID = 5146608031361515552L;

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getTotalNights() {
        return totalNights;
    }

    public void setTotalNights(Integer totalNights) {
        this.totalNights = totalNights;
    }

    public Integer getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public void setRoomPricePerNight(Integer roomPricePerNight) {
        this.roomPricePerNight = roomPricePerNight;
    }

    public Integer getRoomHasOffer() {
        return roomHasOffer;
    }

    public void setRoomHasOffer(Integer roomHasOffer) {
        this.roomHasOffer = roomHasOffer;
    }

    public Integer getSavingsPerRoom() {
        return savingsPerRoom;
    }

    public void setSavingsPerRoom(Integer savingsPerRoom) {
        this.savingsPerRoom = savingsPerRoom;
    }

    public Integer getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(Integer orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(String timeCount) {
        this.timeCount = timeCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
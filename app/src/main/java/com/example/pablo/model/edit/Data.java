package com.example.pablo.model.edit;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable
{
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    @SerializedName("order_id")
    @Expose
    private Long orderId;
    @SerializedName("room_id")
    @Expose
    private Long roomId;
    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("room_count")
    @Expose
    private String roomCount;
    @SerializedName("total_nights")
    @Expose
    private Long totalNights;
    @SerializedName("room_price_per_night")
    @Expose
    private Long roomPricePerNight;
    @SerializedName("room_has_offer")
    @Expose
    private Long roomHasOffer;
    @SerializedName("savings_per_room")
    @Expose
    private Long savingsPerRoom;
    @SerializedName("order_total_price")
    @Expose
    private Long orderTotalPrice;
    private final static long serialVersionUID = -3569648249116656474L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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

    public String getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(String roomCount) {
        this.roomCount = roomCount;
    }

    public Long getTotalNights() {
        return totalNights;
    }

    public void setTotalNights(Long totalNights) {
        this.totalNights = totalNights;
    }

    public Long getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public void setRoomPricePerNight(Long roomPricePerNight) {
        this.roomPricePerNight = roomPricePerNight;
    }

    public Long getRoomHasOffer() {
        return roomHasOffer;
    }

    public void setRoomHasOffer(Long roomHasOffer) {
        this.roomHasOffer = roomHasOffer;
    }

    public Long getSavingsPerRoom() {
        return savingsPerRoom;
    }

    public void setSavingsPerRoom(Long savingsPerRoom) {
        this.savingsPerRoom = savingsPerRoom;
    }

    public Long getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(Long orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

}
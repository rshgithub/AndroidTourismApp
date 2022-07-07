
package com.example.pablo.model.order_details;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotelOrderItem implements Serializable
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
    @SerializedName("room_id")
    @Expose
    private Integer roomId;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("available_rooms")
    @Expose
    private Integer availableRooms;
    @SerializedName("hotel_name")
    @Expose
    private String hotelName;
    @SerializedName("room_image")
    @Expose
    private List<String> roomImage = null;
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
    private Double totalNights;
    @SerializedName("room_price_per_night")
    @Expose
    private Double roomPricePerNight;
    @SerializedName("room_has_offer")
    @Expose
    private Double roomHasOffer;
    @SerializedName("savings_per_room")
    @Expose
    private Double savingsPerRoom;
    @SerializedName("order_total_price")
    @Expose
    private Double orderTotalPrice;
    @SerializedName("created_from")
    @Expose
    private String createdFrom;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

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

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public List<String> getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(List<String> roomImage) {
        this.roomImage = roomImage;
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

    public Double getTotalNights() {
        return totalNights;
    }

    public void setTotalNights(Double totalNights) {
        this.totalNights = totalNights;
    }

    public Double getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public void setRoomPricePerNight(Double roomPricePerNight) {
        this.roomPricePerNight = roomPricePerNight;
    }

    public Double getRoomHasOffer() {
        return roomHasOffer;
    }

    public void setRoomHasOffer(Double roomHasOffer) {
        this.roomHasOffer = roomHasOffer;
    }

    public Double getSavingsPerRoom() {
        return savingsPerRoom;
    }

    public void setSavingsPerRoom(Double savingsPerRoom) {
        this.savingsPerRoom = savingsPerRoom;
    }

    public Double getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(Double orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
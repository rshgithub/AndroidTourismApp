
package com.example.pablo.model.cart;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotelOrderItem implements Serializable
{
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("user_id")
    @Expose
    private Long userId;
    @SerializedName("order_id")
    @Expose
    private Long orderId;
    @SerializedName("room_id")
    @Expose
    private Long roomId;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("available_rooms")
    @Expose
    private Long availableRooms;
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
    private int roomCount;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Long getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Long availableRooms) {
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

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
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

    public double getRoomHasOffer() {
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
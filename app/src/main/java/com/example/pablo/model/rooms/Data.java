
package com.example.pablo.model.rooms;
import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable
{
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("hotel_id")
    @Expose
    private Long hotelId;
    @SerializedName("has_offer")
    @Expose
    private Long hasOffer;
    @SerializedName("available_rooms")
    @Expose
    private Long availableRooms;
    @SerializedName("price_per_night")
    @Expose
    private Long pricePerNight;
    @SerializedName("room_hotel_name")
    @Expose
    private String roomHotelName;
    @SerializedName("room_images")
    @Expose
    private List<String> roomImages = null;
    private final static long serialVersionUID = 8037720816848664657L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(Long hasOffer) {
        this.hasOffer = hasOffer;
    }

    public Long getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Long availableRooms) {
        this.availableRooms = availableRooms;
    }

    public Long getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Long pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getRoomHotelName() {
        return roomHotelName;
    }

    public void setRoomHotelName(String roomHotelName) {
        this.roomHotelName = roomHotelName;
    }

    public List<String> getRoomImages() {
        return roomImages;
    }

    public void setRoomImages(List<String> roomImages) {
        this.roomImages = roomImages;
    }

}
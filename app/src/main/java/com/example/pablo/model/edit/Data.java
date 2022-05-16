
package com.example.pablo.model.edit;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("hotel_id")
    @Expose
    private Integer hotelId;
    @SerializedName("has_offer")
    @Expose
    private String hasOffer;
    @SerializedName("available_rooms")
    @Expose
    private String availableRooms;
    @SerializedName("price_per_night")
    @Expose
    private String pricePerNight;
    @SerializedName("room_hotel_name")
    @Expose
    private String roomHotelName;
    @SerializedName("room_images")
    @Expose
    private List<String> roomImages = null;
    private final static long serialVersionUID = -7673935371544885098L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public String getHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(String hasOffer) {
        this.hasOffer = hasOffer;
    }

    public String getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(String availableRooms) {
        this.availableRooms = availableRooms;
    }

    public String getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(String pricePerNight) {
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
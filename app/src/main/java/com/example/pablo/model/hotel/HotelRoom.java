
package com.example.pablo.model.hotel;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotelRoom implements Serializable
{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("hotel_id")
    @Expose
    private Long hotelId;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("available_rooms")
    @Expose
    private Long availableRooms;
    @SerializedName("price_per_night")
    @Expose
    private Long pricePerNight;
    @SerializedName("has_offer")
    @Expose
    private Long hasOffer;
    @SerializedName("room_hotel_name")
    @Expose
    private String roomHotelName;
    private final static long serialVersionUID = -7585160805451966869L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public Long getHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(Long hasOffer) {
        this.hasOffer = hasOffer;
    }

    public String getRoomHotelName() {
        return roomHotelName;
    }

    public void setRoomHotelName(String roomHotelName) {
        this.roomHotelName = roomHotelName;
    }

}

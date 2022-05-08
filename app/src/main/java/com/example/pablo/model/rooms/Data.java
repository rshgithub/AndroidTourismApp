
package com.example.pablo.model.rooms;
import java.io.Serializable;
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
        private Integer hasOffer;
    @SerializedName("available_rooms")
    @Expose
    private Integer availableRooms;
    @SerializedName("price_per_night")
    @Expose
    private String pricePerNight;
    @SerializedName("room_hotel_name")
    @Expose
    private String roomHotelName;
    private final static long serialVersionUID = -7025485148811939976L;

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

    public Integer getHasOffer() {
        return hasOffer;
    }

    public void setHasOffer(Integer hasOffer) {
        this.hasOffer = hasOffer;
    }

    public Integer getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Integer availableRooms) {
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

}
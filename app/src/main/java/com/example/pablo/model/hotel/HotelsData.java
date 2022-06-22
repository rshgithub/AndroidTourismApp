
package com.example.pablo.model.hotel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotelsData implements Serializable
{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("star")
    @Expose
    private Long star;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("map")
    @Expose
    private String map;
    @SerializedName("rooms_count")
    @Expose
    private Long roomsCount;
    @SerializedName("available_rooms_count")
    @Expose
    private Long availableRoomsCount;
    @SerializedName("advantages_count")
    @Expose
    private Long advantagesCount;
    @SerializedName("hotel_image")
    @Expose
    private String hotelImage;
    @SerializedName("hotel_rooms")
    @Expose
    private List<HotelRoom> hotelRooms = null;
    @SerializedName("hotel_advantages")
    @Expose
    private List<HotelAdvantage> hotelAdvantages = null;
    private final static long serialVersionUID = 4955277136434358852L;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getStar() {
        return star;
    }

    public void setStar(Long star) {
        this.star = star;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Long getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(Long roomsCount) {
        this.roomsCount = roomsCount;
    }

    public Long getAvailableRoomsCount() {
        return availableRoomsCount;
    }

    public void setAvailableRoomsCount(Long availableRoomsCount) {
        this.availableRoomsCount = availableRoomsCount;
    }

    public Long getAdvantagesCount() {
        return advantagesCount;
    }

    public void setAdvantagesCount(Long advantagesCount) {
        this.advantagesCount = advantagesCount;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

    public List<HotelRoom> getHotelRooms() {
        return hotelRooms;
    }

    public void setHotelRooms(List<HotelRoom> hotelRooms) {
        this.hotelRooms = hotelRooms;
    }

    public List<HotelAdvantage> getHotelAdvantages() {
        return hotelAdvantages;
    }

    public void setHotelAdvantages(List<HotelAdvantage> hotelAdvantages) {
        this.hotelAdvantages = hotelAdvantages;
    }

}

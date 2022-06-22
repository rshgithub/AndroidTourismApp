package com.example.pablo.model.popular_hotels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularHotelsExample implements Serializable
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
    private final static long serialVersionUID = 3137263122125294058L;

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

}

package com.example.pablo.model.hotels;

import java.io.Serializable;
import java.util.List;

import com.example.pablo.model.rooms.RoomsExample;
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
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("star")
    @Expose
    private Integer star;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("map")
    @Expose
    private String map;
    @SerializedName("rooms_count")
    @Expose
    private Integer roomsCount;
    @SerializedName("advantages_count")
    @Expose
    private Integer advantagesCount;
    @SerializedName("hotel_image")
    @Expose
    private String hotelImage;
//    @SerializedName("hotel_advantages")
//    @Expose
//    private List<HotelAdvantage> hotelAdvantages = null;
    @SerializedName("hotel_rooms")
    @Expose
    private List<RoomsExample> hotelRooms = null;
    private final static long serialVersionUID = 556375828334548728L;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
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

    public Integer getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(Integer roomsCount) {
        this.roomsCount = roomsCount;
    }

    public Integer getAdvantagesCount() {
        return advantagesCount;
    }

    public void setAdvantagesCount(Integer advantagesCount) {
        this.advantagesCount = advantagesCount;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

//    public List<HotelAdvantage> getHotelAdvantages() {
//        return hotelAdvantages;
//    }
//
//    public void setHotelAdvantages(List<HotelAdvantage> hotelAdvantages) {
//        this.hotelAdvantages = hotelAdvantages;
//    }

    public List<RoomsExample> getHotelRooms() {
        return hotelRooms;
    }

    public void setHotelRooms(List<RoomsExample> hotelRooms) {
        this.hotelRooms = hotelRooms;
    }

}
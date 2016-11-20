package com.example.tns.realestate.models;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TNS on 11/19/2016.
 */

public class ApartmentDetail implements Parcelable {

    private int[] bitmapsResource;
    private String price;
    private String address;
    private String description;
    private Location location;
    private float rating;
    private int numberOfBedRooms;
    private int numberOfBathRooms;
    private float area;

    public ApartmentDetail(int[] bitmaps, String price, String address, String description, Location location, float rating, int[] parameters) {
        this.bitmapsResource = bitmaps;
        this.price = price;
        this.address = address;
        this.description = description;
        this.location = location;
        this.rating = rating;
        this.area = (float) parameters[0];
        this.numberOfBedRooms = parameters[1];
        this.numberOfBathRooms = parameters[2];
    }

    protected ApartmentDetail(Parcel in) {
        bitmapsResource = in.createIntArray();
        price = in.readString();
        address = in.readString();
        description = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        rating = in.readFloat();
        numberOfBedRooms = in.readInt();
        numberOfBathRooms = in.readInt();
        area = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(bitmapsResource);
        dest.writeString(price);
        dest.writeString(address);
        dest.writeString(description);
        dest.writeParcelable(location, flags);
        dest.writeFloat(rating);
        dest.writeInt(numberOfBedRooms);
        dest.writeInt(numberOfBathRooms);
        dest.writeFloat(area);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ApartmentDetail> CREATOR = new Creator<ApartmentDetail>() {
        @Override
        public ApartmentDetail createFromParcel(Parcel in) {
            return new ApartmentDetail(in);
        }

        @Override
        public ApartmentDetail[] newArray(int size) {
            return new ApartmentDetail[size];
        }
    };

    public int[] getBitmapsResource() {
        return bitmapsResource;
    }

    public void setBitmapsResource(int[] bitmapsResource) {
        this.bitmapsResource = bitmapsResource;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberOfBedRooms() {
        return numberOfBedRooms;
    }

    public void setNumberOfBedRooms(int numberOfBedRooms) {
        this.numberOfBedRooms = numberOfBedRooms;
    }

    public int getNumberOfBathRooms() {
        return numberOfBathRooms;
    }

    public void setNumberOfBathRooms(int numberOfBathRooms) {
        this.numberOfBathRooms = numberOfBathRooms;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }
}

package com.example.tns.realestate.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TNS on 11/27/2016.
 */

public class Provider implements Parcelable {
    private String name;
    private String address;
    private String contact;

    public Provider(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    protected Provider(Parcel in) {
        name = in.readString();
        address = in.readString();
        contact = in.readString();
    }

    public static final Creator<Provider> CREATOR = new Creator<Provider>() {
        @Override
        public Provider createFromParcel(Parcel in) {
            return new Provider(in);
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(contact);
    }
}

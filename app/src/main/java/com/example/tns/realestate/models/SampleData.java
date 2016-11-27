package com.example.tns.realestate.models;

import android.content.Context;
import android.location.Location;

import com.example.tns.realestate.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tagmanager.DataLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by TNS on 11/19/2016.
 * Sample data for application
 */

public class SampleData {
    private Context context;

    public SampleData(Context context) {
        this.context = context;
        this.initializeData();
    }


    private List<ApartmentDetail> sampleData = new ArrayList<>();

    /**
     * Create sample
     */
    private void initializeData() {


        int[] premiumTypes = new int[]{R.drawable.premium_1, R.drawable.premium_2, R.drawable.premium_3};
        int[] type1s = new int[]{R.drawable.type1_1, R.drawable.type1_2, R.drawable.type1_3};
        int[] type2s = new int[]{R.drawable.type2_1, R.drawable.type2_2, R.drawable.type2_3};
        int[] type3s = new int[]{R.drawable.type3_1, R.drawable.type3_2, R.drawable.type3_3};
        this.sampleData.add(new ApartmentDetail(premiumTypes, "$1,500,000,0000 VNĐ", "Bến nghế - Quận 1 - Hồ Chí Minh", this.context.getString(R.string.sample_description)
                , null, 1.5f, new int[]{60, 2, 3}, new Provider("Flemington Tower", "Đường Trần Đại Nghĩa, P Tân Tạo A, Q Bình Tân, TPHCM ", "(08) 3962 1599")));
        this.sampleData.add(new ApartmentDetail(type1s, "$1,100,000,0000 VNĐ", "Quận Tân Bình - Hồ Chí Minh", this.context.getString(R.string.sample_description), null, 0.8f, new int[]{60, 2, 3},
                new Provider("Saigon South Residences", "Nguyễn Hữu Thọ, Xã Phước Kiển, Nhà Bè, Hồ Chí Minh", "(08) 3962 1599")));
        this.sampleData.add(new ApartmentDetail(type2s, "$1,000,000,0000 VNĐ", "Linh Trung - Thủ Đức - Hồ Chí Minh", this.context.getString(R.string.sample_description), null, 1.0f, new int[]{60, 2, 3},
                new Provider("Flemington Tower", "Nguyễn Hữu Thọ, Xã Phước Kiển, Nhà Bè, Hồ Chí Minh", "0902587529")));
        this.sampleData.add(new ApartmentDetail(type3s, "$900,000,0000 VNĐ", "Thủ dầu một - Bình Dương", this.context.getString(R.string.sample_description), null, 1.4f, new int[]{45, 2, 3},
                new Provider("Flemington Tower", "Đường Trần Đại Nghĩa, P Tân Tạo A, Q Bình Tân, TPHCM ", "(08) 3962 1599")));
    }

    public List<ApartmentDetail> getSampleData() {
        return this.sampleData;
    }

    /**
     * Create sample random locations base on user current location
     *
     * @param userCurrentLocation : user current {@link Location}
     * @return : {@link List} of sample random {@link LatLng}(s)
     */
    public List<LatLng> sampleLocationSurroundingUser(Location userCurrentLocation) {
        List<LatLng> listOfRandomLocations = new ArrayList<>();
        final double currentLatitude = userCurrentLocation.getLatitude();
        final double currentLongitude = userCurrentLocation.getLongitude();
        // start to add 4 random LatLng(s) into list
        listOfRandomLocations.add(new LatLng(currentLatitude + Math.random(), currentLongitude + Math.random()));
        listOfRandomLocations.add(new LatLng(currentLatitude - Math.random(), currentLongitude - Math.random()));
        listOfRandomLocations.add(new LatLng(currentLatitude + Math.random(), currentLongitude - Math.random()));
        listOfRandomLocations.add(new LatLng(currentLatitude - Math.random(), currentLongitude + Math.random()));
        // return the list
        return listOfRandomLocations;
    }
}

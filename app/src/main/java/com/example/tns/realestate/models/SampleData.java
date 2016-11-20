package com.example.tns.realestate.models;

import android.content.Context;

import com.example.tns.realestate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TNS on 11/19/2016.
 */

public class SampleData {
    private Context context;

    public SampleData(Context context) {
        this.context = context;
        this.initializeData();
    }


    private List<ApartmentDetail> sampleData = new ArrayList<>();

    private void initializeData() {


        int[] premiumnTypes = new int[]{R.drawable.premium_1, R.drawable.premium_2, R.drawable.premium_3};
        int[] type1s = new int[]{R.drawable.type1_1, R.drawable.type1_2, R.drawable.type1_3};
        int[] type2s = new int[]{R.drawable.type2_1, R.drawable.type2_2, R.drawable.type2_3};
        int[] type3s = new int[]{R.drawable.type3_1, R.drawable.type3_2, R.drawable.type3_3};
        this.sampleData.add(new ApartmentDetail(premiumnTypes, "$1,500,000,0000 VNĐ", "Bến nghế - Quận 1 - Hồ Chí Minh", this.context.getString(R.string.sample_description), null, 1.5f, new int[]{60, 2, 3}));
        this.sampleData.add(new ApartmentDetail(type1s, "$1,100,000,0000 VNĐ", "Quận Tân Bình - Hồ Chí Minh", this.context.getString(R.string.sample_description), null, 0.8f, new int[]{60, 2, 3}));
        this.sampleData.add(new ApartmentDetail(type2s, "$1,000,000,0000 VNĐ", "Linh Trung - Thủ Đức - Hồ Chí Minh", this.context.getString(R.string.sample_description), null, 1.0f, new int[]{60, 2, 3}));
        this.sampleData.add(new ApartmentDetail(type3s, "$900,000,0000 VNĐ", "Thủ dầu một - Bình Dương", this.context.getString(R.string.sample_description), null, 1.4f, new int[]{45, 2, 3}));
    }

    public List<ApartmentDetail> getSampleData() {
        return this.sampleData;
    }
}

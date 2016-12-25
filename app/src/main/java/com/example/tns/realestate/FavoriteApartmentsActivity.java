package com.example.tns.realestate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.tns.realestate.adapters.FavoriteApartmentAdapter;
import com.example.tns.realestate.models.ApartmentDetail;
import com.example.tns.realestate.models.SampleData;

import java.util.ArrayList;
import java.util.List;

public class FavoriteApartmentsActivity extends AppCompatActivity {

    private ListView listView;
    private FavoriteApartmentAdapter favoriteApartmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_apartments);

        this.listView = (ListView) this.findViewById(R.id.listview_favorite_apartments);
        SampleData sampleData = new SampleData(this);
        List<ApartmentDetail> listOfApartmentDetails = new ArrayList<>();
        listOfApartmentDetails.addAll(sampleData.getSampleData());
        listOfApartmentDetails.addAll(sampleData.getSampleData());
        listOfApartmentDetails.addAll(sampleData.getSampleData());
        this.favoriteApartmentAdapter = new FavoriteApartmentAdapter(this, listOfApartmentDetails);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.listView.setAdapter(this.favoriteApartmentAdapter);
    }
}

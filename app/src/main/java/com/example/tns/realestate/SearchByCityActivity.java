package com.example.tns.realestate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.tns.realestate.adapters.SearchByCityAdapter;
import com.example.tns.realestate.models.ApartmentDetail;
import com.example.tns.realestate.models.SampleData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchByCityActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    private ImageView imageView;
    private SearchByCityAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set up sample data for spinner
        final Spinner spinnerCitySelector = (Spinner) this.findViewById(R.id.spinner_city_chooser);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Hà Nội", "Huế", "Đà Nẵng", "Sài Gòn"});
        spinnerCitySelector.setAdapter(adapter);
        // register item selected listener
        spinnerCitySelector.setOnItemSelectedListener(this);


        this.imageView = (ImageView) this.findViewById(R.id.city_header);

        // sample data for adapter
        SampleData sampleData = new SampleData(this);
        List<ApartmentDetail> listOfSampleApartments = sampleData.getSampleData();
        listOfSampleApartments.addAll(sampleData.getSampleData()); // doubles the data
        // recycler view
        this.recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view_search_by_city);
        this.recyclerViewAdapter = new SearchByCityAdapter(listOfSampleApartments, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setAutoMeasureEnabled(true);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(this.recyclerViewAdapter);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String selectedCity = (String) parent.getItemAtPosition(position);
        if (position == 0) { // Ha noi
            Picasso.with(this)
                    .load(R.drawable.ha_noi_city)
                    .fit()
                    .into(this.imageView);

        } else if (position == 1) { // Hue
            Picasso.with(this)
                    .load(R.drawable.hue_city)
                    .fit()
                    .into(this.imageView);

        } else if (position == 2) { // Da nang
            Picasso.with(this)
                    .load(R.drawable.da_nang_city)
                    .fit()
                    .into(this.imageView);

        } else if (position == 3) { // Sai gon
            Picasso.with(this)
                    .load(R.drawable.sai_gon_city)
                    .fit()
                    .into(this.imageView);

        }
        this.reloadRecyclerViewData();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * Reload sample data when user change item of {@link Spinner}. This demonstrates how recycler view updates data
     */
    private void reloadRecyclerViewData() {
        this.recyclerViewAdapter.getListOfApartments().clear(); // clear all old data
        SampleData sampleData = new SampleData(this);
        this.recyclerViewAdapter.getListOfApartments().addAll(sampleData.getSampleData());
        this.recyclerViewAdapter.getListOfApartments().addAll(sampleData.getSampleData());
        this.recyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_search_by_city_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_on_top) {
            this.recyclerView.smoothScrollToPosition(0);
            this.recyclerView.getLayoutManager().scrollToPosition(0);
        }
        return super.onOptionsItemSelected(item);
    }
}

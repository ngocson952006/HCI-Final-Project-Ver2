package com.example.tns.realestate;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SearchByCityActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    private ActionBar actionBar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.actionBar = this.getSupportActionBar();

        // set up sample data for spinner
        final Spinner spinnerCitySelector = (Spinner) this.findViewById(R.id.spinner_city_chooser);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Hà Nội", "Huế", "Đà Nẵng", "Sài Gòn"});
        spinnerCitySelector.setAdapter(adapter);
        // register item selected listener
        spinnerCitySelector.setOnItemSelectedListener(this);


        this.imageView = (ImageView) this.findViewById(R.id.city_header);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedCity = (String) parent.getItemAtPosition(position);
        if (position == 0) { // Ha noi
            Picasso.with(this)
                    .load(R.drawable.ha_noi_city)
                    .fit()
                    .into(this.imageView);

        } else if (position == 1) { // Ha noi
            Picasso.with(this)
                    .load(R.drawable.hue_city)
                    .fit()
                    .into(this.imageView);

        } else if (position == 2) { // Ha noi
            Picasso.with(this)
                    .load(R.drawable.da_nang_city)
                    .fit()
                    .into(this.imageView);

        } else if (position == 3) { // Ha noi
            Picasso.with(this)
                    .load(R.drawable.sai_gon_city)
                    .fit()
                    .into(this.imageView);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

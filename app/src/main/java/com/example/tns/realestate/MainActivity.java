package com.example.tns.realestate;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tns.realestate.adapters.PrimaryApartmentAdapter;
import com.example.tns.realestate.customviews.TransientFloatingActionButton;
import com.example.tns.realestate.models.SampleData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName(); // tag

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // shared preference keys
    private static final String USER_LAST_KNOWN_LOCATION_SHARED_PREFERENCE_KEY = "user_last_known_location_shared_preference_key";
    private static final String USER_LAST_KNOWN_LOCATION_ADDRESS_SHARED_PREFERENCE_KEY = "user_last_known_location_address_shared_preference_key";


    private RecyclerView recyclerView;
    private RecyclerView.Adapter primaryApartmentAdapters;
    private RecyclerView.LayoutManager layoutManager;
    private SampleData sampleData;
    private Location lastKnownLocation;
    private GoogleApiClient googleApiClient;
    private MaterialSheetFab materialSheetFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.colorAccent);

        TransientFloatingActionButton fab = (TransientFloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //   .setAction("Action", null).show();

                materialSheetFab.showFab(0, getResources().getDimensionPixelSize(R.dimen.snackbar_height));
            }
        });

        // prepare material Sheet Floating Action button
        this.materialSheetFab = new MaterialSheetFab(fab, sheetView, overlay, sheetColor, fabColor);
        // register listener
        this.materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                super.onShowSheet();
            }

            @Override
            public void onHideSheet() {
                super.onHideSheet();
            }
        });

        // register transient menu item click listener
        this.findViewById(R.id.textview_fab_menu_search_by_city).setOnClickListener(this);
        this.findViewById(R.id.textview_fab_menu_search_by_current_location).setOnClickListener(this);
        this.findViewById(R.id.textview_fab_menu_search_by_price).setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /**
         * Prepare sample data
         */
        this.sampleData = new SampleData(this);
        /**
         * Prepare recycler view
         */


        this.recyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        this.primaryApartmentAdapters = new PrimaryApartmentAdapter(this, this.sampleData.getSampleData());
        this.layoutManager = new LinearLayoutManager(this);
        // divider for items of recycler view
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.recyclerView.setAdapter(this.primaryApartmentAdapters);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            },
                            LOCATION_PERMISSION_REQUEST_CODE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            this.initializeGoogleApiClient();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        // hid the material fab sheet
        if (this.materialSheetFab.isSheetVisible()) {
            this.materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            // Handle the camera action
        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_maps) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        this.googleApiClient.connect();
        super.onStart();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //  this.initializeGoogleApiClient();
                    // connect
                    // this.googleApiClient.connect();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }

        // other 'case' lines to check for other
        // permissions this app might request

    }


    private void initializeGoogleApiClient() {
        // Create an instance of GoogleAPIClient.
        if (this.googleApiClient == null) {
            this.googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        // Get user's last known location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "Visualizing location");
            this.lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
            // make sure location we've found is valid
            if (this.lastKnownLocation != null) {

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(this.lastKnownLocation.getLatitude(),
                            this.lastKnownLocation.getLongitude(),
                            1 // 1 : we just a single address
                    );
                    // display on View
                    Address currentAddress = addresses.get(0);
                    final TextView textViewCurrentAddress = (TextView) this.findViewById(R.id.textview_current_location);
                    // visualize the address locality
                    textViewCurrentAddress.setText(currentAddress.getLocality() + " - " + currentAddress.getAddressLine(0) + " - " + currentAddress.getCountryName());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        this.googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.textview_fab_menu_search_by_price) {
            Intent detailIntent = new Intent(this, SearchByPriceActivity.class);

            // hide the fab sheet.
            this.materialSheetFab.hideSheet();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                Bundle transientBundle = ActivityOptions.makeCustomAnimation(this, android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right).toBundle();
                this.startActivity(detailIntent, transientBundle);
            } else {
                this.startActivity(detailIntent);
            }
        }
    }
}

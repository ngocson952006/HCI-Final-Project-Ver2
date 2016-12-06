package com.example.tns.realestate;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName(); // tag

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // shared preference keys
    private static final String USER_LAST_KNOWN_LOCATION_SHARED_PREFERENCE_KEY = "user_last_known_location_shared_preference_key";
    private static final String USER_LAST_KNOWN_LOCATION_ADDRESS_SHARED_PREFERENCE_KEY = "user_last_known_location_address_shared_preference_key";
    // public shared intent keys
    public static final String USER_CURRENT_LOCATION = "user_current_location";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter primaryApartmentAdapters;
    private RecyclerView.LayoutManager layoutManager;
    private SampleData sampleData;
    private Location lastKnownLocation;
    private GoogleApiClient googleApiClient;
    private MaterialSheetFab materialSheetFab;
    //   private Handler locationHandler;

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
        this.recyclerView.setHasFixedSize(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
        this.initializeGoogleApiClient();
        // hanlder to hanlde location
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

        } else if (id == R.id.nav_contact) { // contact
            Intent contactIntent = new Intent(this, ContactUsActivity.class);
            this.startIntentSlideInLeft(contactIntent);
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
        // instance a LocationHelper
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
                    this.initializeGoogleApiClient();
                    // connect
                    this.googleApiClient.connect();

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
        Log.d(TAG, "Visualizing location");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(this.googleApiClient);
        // make sure location we've found is valid
        if (this.lastKnownLocation != null) {
            // start new async task to update text view
            new AsyncTask<Void, Void, Address>() {
                @Override
                protected Address doInBackground(Void... params) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude(),
                                1 // 1 : we just a single address
                        );
                        // display on View
                        return addresses.get(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage());
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Address address) {
                    final TextView textViewCurrentAddress = (TextView) findViewById(R.id.textview_current_location);
                    // visualize the address locality
                    textViewCurrentAddress.setText(address.getLocality() + " - " + address.getAddressLine(0) + " - " + address.getCountryName());

                }
            }.execute();


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
        //this.googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.textview_fab_menu_search_by_price) {
            Intent searchByPricelIntent = new Intent(this, SearchByPriceActivity.class);

            // hide the fab sheet.
            this.materialSheetFab.hideSheet();
            this.startIntentSlideInLeft(searchByPricelIntent);
        } else if (viewId == R.id.textview_fab_menu_search_by_current_location) {
            // navigate to maps activity
            Intent googleMapsIntent = new Intent(this, MapsActivity.class);
            // put user current location into intent
            googleMapsIntent.putExtra(USER_CURRENT_LOCATION, this.lastKnownLocation);
            // hide the fab sheet.
            this.materialSheetFab.hideSheet();
            this.startIntentSlideInLeft(googleMapsIntent);
        } else if (viewId == R.id.textview_fab_menu_search_by_city) {
            Intent searchByCityIntent = new Intent(this, SearchByCityActivity.class);

            // hide the fab sheet.
            this.materialSheetFab.hideSheet();
            this.startIntentSlideInLeft(searchByCityIntent);
        }
    }


    /**
     * Start an intent with slide in left transient
     *
     * @param destinationIntent : the intent that will be navigated
     */
    private void startIntentSlideInLeft(Intent destinationIntent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Bundle transientBundle = ActivityOptions.makeCustomAnimation(this, android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right).toBundle();
            this.startActivity(destinationIntent, transientBundle);
        } else {
            this.startActivity(destinationIntent);
        }
    }

}

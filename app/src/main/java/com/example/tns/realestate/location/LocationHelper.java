package com.example.tns.realestate.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationProvider;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by TNS on 12/3/2016.
 *
 * @author TNS
 *         This class provides utility methods to work with {@link android.location.Location} such as : get last known location, location listener
 */

public class LocationHelper {

    private static final String TAG = LocationHelper.class.getSimpleName(); // tag

    // public message Ids
    public static final int MESSAGE_LOCATION_FOUND_ID = 1;
    public static final int MESSAGE_LOCATION_NOT_FOUND_ID = 2;
    //  If the most recent location is available for the FINE provider, and it is relatively
    //  recent (within FIX_RECENT_BUFFER_TIME -- currently 30 seconds), it is returned back to
    //  the caller using a Message indicating the results.
    private static final int FIX_RECENT_BUFFER_TIME = 5000;


    private Context context;
    private LocationManager locationManager;
    private Handler handler;
    private Runnable handlerCallbackRunnable;
    private String providerName;
    private LocationListener locationListener; //

    /**
     * Member constructor with parameters
     *
     * @param context : {@link LocationManager} initialized from a {@link android.app.Activity}
     * @param handler : the {@link Handler} to send messages
     */
    public LocationHelper(Context context, Handler handler) {
        this.context = context;
        this.locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        this.handler = handler;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        this.providerName = this.locationManager.getBestProvider(criteria, true);
        //
        this.locationListener = new LocationListenerImpl();
        this.handlerCallbackRunnable = new Runnable() {
            @Override
            public void run() {
                finishListeningLocationChanges(null);
            }
        };

    }

    /**
     * Find current {@link Location} via {@link LocationManager}. After that, send this location into {@link Message}
     */
    public void getCurrentLocation(int duration) {
        Log.d(TAG, "Start to get current location");
        // make sure we have valid provider name
        if (this.providerName == null) {
            Log.e(TAG, "provider name is valid, could not find last known location");
            this.finishListeningLocationChanges(null);
        } else {
            // first check last KNOWN location (and if the fix is recent enough, use it)
            // NOTE -- this does NOT WORK in the Emulator
            // (if you send a DDMS "manual" time or geo fix, you get correct DATE,
            // but fix time starts at 00:00 and seems to increment by 1 second each time sent)
            // to test this section (getLastLocation being recent enough), you need to use a real device
            // request permission if device's android version is M or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d(TAG, "Device's Android version is >= M");
                if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Permission denied");
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            }
            Location lastKnownLocation = this.locationManager.getLastKnownLocation(this.providerName);
            // make sure location is valid
            if (lastKnownLocation != null) {
                Log.d(TAG, "Last known location : " + lastKnownLocation.toString());
                // send location into message
                this.sendLocationIntoMessage(MESSAGE_LOCATION_FOUND_ID, (int) (lastKnownLocation.getLatitude() * 1e6), (int) (lastKnownLocation.getLongitude() * 1e6));
                //start to listen location changes
            } else {
                Log.d(TAG, "Last known location is NULL");
                this.listenForLocationChanges(this.providerName, duration);
            }
        }

    }


    private void listenForLocationChanges(String providerName, int updateDuration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }
        this.locationManager.requestLocationUpdates(providerName, 0, 0, this.locationListener);
        this.handler.postDelayed(this.handlerCallbackRunnable, updateDuration * 1000);

    }

    /**
     * Finish listening for location changes. This includes remove registering update from {@link LocationListener}, remove all {@link Handler} works.
     * NOTE : what is 1E6 ?
     * The Location object in Android stores latitude and
     * longitude as type double. Other classes, such as GeoPoint, which we’ll see in
     * the maps API soon, use type int. Both represent the same location; the integer type is the microdegrees representation.
     * The 1E6 means multiply the double value by 1 million. You can multiply or divide by 1E6 to go back and forth
     * between the two representations.
     */
    private void finishListeningLocationChanges(Location changedLocation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }
        this.locationManager.removeUpdates(this.locationListener);
        this.handler.removeCallbacks(this.handlerCallbackRunnable);
        // start send current location
        if (changedLocation != null) {
            this.sendLocationIntoMessage(MESSAGE_LOCATION_FOUND_ID, (int) (changedLocation.getLatitude() * 1e6), (int) (changedLocation.getLongitude() * 1e6));
        } else {
            // if location is null, return 0,0
            this.sendLocationIntoMessage(MESSAGE_LOCATION_NOT_FOUND_ID, 0, 0);
        }
    }

    /**
     * Obtain a {@link Message} and send via handler
     *
     * @param messageId : message id to be sent.
     * @param latitude  : information of location's latitude to be sent.
     * @param longitude :information of location's longitude to be sent.
     */
    private void sendLocationIntoMessage(int messageId, int latitude, int longitude) {
        Message message = Message.obtain(this.handler, messageId, latitude, longitude);
        this.handler.sendMessage(message);
    }


    /**
     * An implementation of a {@link LocationListener}
     */
    private class LocationListenerImpl implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // make sure we have valid location
            if (location != null) {
                // log the location
                Log.d(TAG, "Location changed !!");
                finishListeningLocationChanges(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "location changed. Status : " + status);
            // analyze status
            switch (status) {
                case LocationProvider.AVAILABLE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    finishListeningLocationChanges(null);
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            finishListeningLocationChanges(null);
        }
    }


}

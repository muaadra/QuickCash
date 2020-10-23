package com.softeng.quickcash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * this class returns the location of the device
 * to implement: instantiate once it in your activity and override the
 * onRequestPermissionsResult method in your activity
 * see LocationDemo for a demo on how to implement, otherwise
 * feel free to implement it your way
 */

public abstract class MyLocation {

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    private LocationCallback locationCallback;
    Activity activity;
    LongLatLocation lastLocation;


    public MyLocation(Activity activity) {
        if(activity != null){
            this.activity = activity;
            locationRequestSetup();
            getLocation();
        }
    }

    /**
     * calculate the distance from my location to other based on Long. and Lat.
     * @return the distance in meters, -1 if no last location
     */
    public float calcDistanceToLocation(LongLatLocation otherLocation){
        if(lastLocation != null){
            float[] results = new float[10];

            Location.distanceBetween(lastLocation.getLatitude(),
                    lastLocation.getLongitude(),otherLocation.getLatitude(),
                    otherLocation.getLongitude(),results);

            return results[0];
        }
        return -1;
    }

    private  void locationRequestSetup(){
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getLocation() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();

                lastLocation = new LongLatLocation(location.getLatitude(),
                        location.getLongitude());
                LocationResult(location);

                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }

        };

        fusedLocationProviderClient = LocationServices.
                getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //request permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                activity.requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},99);
            }
        }else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        }

    }


    public abstract void LocationResult(Location location);


}

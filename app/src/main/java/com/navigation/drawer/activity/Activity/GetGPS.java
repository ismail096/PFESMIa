package com.navigation.drawer.activity.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Pro on 18/03/2017.
 */

public class GetGPS {
    public static Location location = null ;
    LocationManager locationManager;
    LocationListener locationListener;
    public GetGPS(Activity activity){
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                GetGPS.location = location ;

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };

        // If device is running SDK < 23

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // ask for permission

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                // we have permission!

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }
    }
}

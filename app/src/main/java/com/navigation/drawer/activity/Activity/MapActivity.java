package com.navigation.drawer.activity.Activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.navigation.drawer.activity.Activity.Profiles.CliniqueProfil;
import com.navigation.drawer.activity.Activity.Profiles.MedecinProfil;
import com.navigation.drawer.activity.Activity.Profiles.PharmacieProfil;
import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.Maps.DirectionFinder;
import com.navigation.drawer.activity.Maps.DirectionFinderListener;
import com.navigation.drawer.activity.Maps.Route;
import com.navigation.drawer.activity.R;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MapActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener, GoogleMap.OnMarkerClickListener {

    public static Object currentObject = null ;
    public static Location currentLocation  = null ;
    public static Location searchedLocation = null ;

    LocationManager locationManager;

    LocationListener locationListener;

    HashMap<MarkerOptions,Marker> list = new HashMap<>();

    Marker destMarker = null;


    private GoogleMap mMap;

    private List<MarkerOptions> originMarkers = new ArrayList<>();
    private List<MarkerOptions> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportM;apFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if(currentLocation != null)
            sendRequest();

        ////////////////////////////////////

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if(currentLocation == null){
                    currentLocation = new Location("");
                    currentLocation.setLongitude(location.getLongitude());
                    currentLocation.setLatitude(location.getLatitude());
                    startActivity(new Intent(MapActivity.this, MapActivity.class));
                }
                else {
                    if (currentLocation.distanceTo(location) > 200) {
                        currentLocation.setLongitude(location.getLongitude());
                        currentLocation.setLatitude(location.getLatitude());
                        startActivity(new Intent(MapActivity.this, MapActivity.class));
                    }
                }
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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // ask for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                // we have permission!

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }

        }

    }

    private void sendRequest() {
            String destination = searchedLocation.getLatitude() + "," + searchedLocation.getLongitude();
            String origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();

            try {
                new DirectionFinder(this, origin, destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(34.0197992,-5.018394);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (MarkerOptions marker : originMarkers) {
                list.get(marker).remove();
            }
        }

        if (destinationMarkers != null) {
            for (MarkerOptions marker : destinationMarkers) {
                list.get(marker).remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        mMap.setOnMarkerClickListener(this);
        for (Route route : routes) {

            originMarkers.add(new MarkerOptions().position(route.startLocation).title("My current position").snippet("")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));
            String MarkerName = "" ;
            if(currentObject.getClass().getSimpleName().equals("Pharmacie")) {
                Pharmacie phar = (Pharmacie) currentObject ;
                MarkerName = phar.getPharmacie();
            }
            else{
                if(currentObject.getClass().getSimpleName().equals("Medecin")) {
                    Medecin med = (Medecin) currentObject;
                    MarkerName = med.getName();
                }
                else {
                    Clinique cl = (Clinique) currentObject;
                    MarkerName = cl.getName();
                }
            }
            destinationMarkers.add(new MarkerOptions().position(route.endLocation).title("Cliquez pour plus d'information").snippet("")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pharmacyloc)));



            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (MarkerOptions marker : originMarkers) {
                Marker marker1 = mMap.addMarker(marker);
                list.put(marker,marker1);
                marker1.showInfoWindow();
                builder.include(marker.getPosition());
            }
            for (MarkerOptions marker : destinationMarkers) {
                Marker marker1 = mMap.addMarker(marker);
                list.put(marker,marker1);
                marker1.showInfoWindow();
                builder.include(marker.getPosition());
            }

            destMarker = list.get(destinationMarkers.get(0));

            LatLngBounds bounds = builder.build();
            int padding = 100; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);

            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);



            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.RED).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(destMarker))
        {
            if(currentObject.getClass().getSimpleName().equals("Pharmacie")) {
                Pharmacie phar = (Pharmacie) currentObject ;
                PharmacieProfil.currentPharmacie = phar;
                startActivity(new Intent(this,PharmacieProfil.class));
            }
            else{
                if(currentObject.getClass().getSimpleName().equals("Medecin")) {
                    Medecin med = (Medecin) currentObject;
                    MedecinProfil.currentMedecin = med;
                    startActivity(new Intent(this,MedecinProfil.class));
                }
                else {
                    Clinique cl = (Clinique) currentObject;
                    CliniqueProfil.currentClinique = cl;
                    startActivity(new Intent(this,CliniqueProfil.class));
                }
            }

        }
        return true ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

    }

}

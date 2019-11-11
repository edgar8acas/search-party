package com.androidcourse.searchparty;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.Random;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationUpdate {

    private GoogleMap mMap;
    private Polyline route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /*public void genLatLng(View view){
        Random r = new Random();

        double lat = (r.nextDouble() * 360) - 180;
        double lng = (r.nextDouble() * 360) - 180;

        Log.d("Latitude: ", "" + lat);
        Log.d("Longitude: ", "" + lng);


        updateMap(new LatLng(lat,lng));
    }*/


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final Handler handler = new Handler();
        final LocationUpdaterBackground task = new LocationUpdaterBackground(this);
        handler.post(new Runnable(){
            @Override
            public void run() {
                task.execute();
                handler.postDelayed(this, 60000);
            }
        });

    }

    public void updateMap(Location l){
        double lat = l.getLatitude();
        double lng = l.getLongitude();

        LatLng location = new LatLng(lat,lng);
        if(route == null){
            route = mMap.addPolyline(new PolylineOptions()
                    .clickable(false)
                    .add(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
        else{
            List<LatLng> points = route.getPoints();
            points.add(location);
            route.setPoints(points);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
    }

    public Activity getActivity(){
        return this;
    }
}

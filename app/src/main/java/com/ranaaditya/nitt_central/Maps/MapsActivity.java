package com.ranaaditya.nitt_central.Maps;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ranaaditya.nitt_central.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,TaskLoadedCallback {

    private GoogleMap mMap;
    Polyline currentPolyline;
    MarkerOptions place1,place2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

         place1=new MarkerOptions().position(new LatLng(27.658143,85.3199503)).title("E-rickshaw");
         place2=new MarkerOptions().position(new LatLng(27.664791,85.3208583)).title("you");

//        String url=geturl(place1.getPosition(),place2.getPosition(),"driving");
//
//        new FetchURL(MapsActivity.this).execute(url, "driving");


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        String url=geturl(place1.getPosition(),place2.getPosition(),"driving");
        new FetchURL(MapsActivity.this).execute(url, "driving");
//        GoogleDirection.withServerKey("AIzaSyCHqrKrONfPbYsVk-YUfavQo0L60IsXMHg")
//                .from(new LatLng(37.7681994, -122.444538))
//                .to(new LatLng(37.7749003,-122.4034934))
//                .avoid(AvoidType.FERRIES)
//                .avoid(AvoidType.HIGHWAYS)
//                .execute(new DirectionCallback() {
//                    @Override
//                    public void onDirectionSuccess(Direction direction) {
//                        if(direction.isOK()) {
//                            // Do something
//                        } else {
//                            // Do something
//                        }
//                    }
//
//                    @Override
//                    public void onDirectionFailure(Throwable t) {
//                        // Do something
//                    }
//                });

    }
    private String geturl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);

        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}

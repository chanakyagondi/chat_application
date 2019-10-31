package com.example.friendly_chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    String str;
    private Button mshare;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private String mUsername;
    Intent intent_g;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        mshare=(Button) findViewById(R.id.shareButton);
        intent_g=getIntent();
        mUsername=intent_g.getStringExtra("key");
        mFirebaseDatabase= FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference().child("messages");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            Log.i("map_test", "onCreate: 111");
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    LatLng loc = new LatLng(lat, lon);
                    Log.i("map_test", "onCreate: 112"+lat+" "+lon);
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                        Log.i("map_test", "onCreate: 113"+lat+" "+lon);
                        str=addresses.get(0).getAddressLine(0);
                        Log.i("map_test", "onCreate: 114"+str);
                        mMap.addMarker(new MarkerOptions().position(loc).title(str));
                        Log.i("map_test", "onCreate: 115"+str);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,11.0f));
                        Log.i("map_test", "onCreate: 116"+str);
                    }
                    catch (IOException io){
                        io.printStackTrace();
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
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    LatLng loc = new LatLng(lat, lon);
                    Log.i("map_test", "onCreate1: 112"+lat+" "+lon);
                    Geocoder geocoder=new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                        Log.i("map_test", "onCreate: 113"+lat+" "+lon);
                        String str=addresses.get(0).getLocality()+"  "+addresses.get(0).getCountryName();
                        Log.i("map_test", "onCreate1: 114"+str);
                        mMap.addMarker(new MarkerOptions().position(loc).title(str));
                        Log.i("map_test", "onCreate1: 115"+str);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,11.0f));
                        Log.i("map_test", "onCreate1: 116"+str);
                    }
                    catch (IOException io){
                        io.printStackTrace();
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
            });
        }
        mshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                Log.i("maps_ddemo", "onClick: hiiiiiiiii"+mUsername);
                FriendlyMessage friendlyMessage = new FriendlyMessage(str, mUsername, null);
                mDatabaseReference.push().setValue(friendlyMessage);
                Intent myIntent = new Intent(MapsActivity.this,MainActivity.class);
                MapsActivity.this.startActivity(myIntent);
                // Clear input box
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,11.0f));
    }


}

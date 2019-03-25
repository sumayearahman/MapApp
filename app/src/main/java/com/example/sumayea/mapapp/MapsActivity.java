package com.example.sumayea.mapapp;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.intentfilter.androidpermissions.PermissionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import im.delight.android.location.SimpleLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SimpleLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        List<String> permis = new ArrayList<>();
        permis.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permis.add(Manifest.permission.ACCESS_FINE_LOCATION);


        PermissionManager permissionManager = PermissionManager.getInstance(getApplicationContext());
        permissionManager.checkPermissions(permis, new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(getApplicationContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        });





        location = new SimpleLocation(this);

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


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

        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();
        // Add a marker in Sydney and move the camera
        LatLng myLatlang = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLatlang).title("Marker in dhaka"));
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(dhaka));
        float zoomLevel = (float) 15.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatlang, zoomLevel));
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        try {
            getAddress(latitude, longitude);
        } catch (Exception e) {
//            Toast.makeText(this, "lat: " + latitude + "Lon :" + longitude, Toast.LENGTH_SHORT).show();

        }
    }
    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Toast.makeText(this,""+add,Toast.LENGTH_LONG).show();

            Log.v("IGA", "Address" + add);

// Toast.makeText(this, "Address=>" + add,
// Toast.LENGTH_SHORT).show();

// TennisAppActivity.showDialog(add);
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

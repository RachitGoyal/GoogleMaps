package com.osahub.rachit.location;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class LocationFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        setUpMap();
        addingMarkers();
        addingCircles();
        setOnLongClickListenerForAddingNewMarkers();
        return v;
    }

    private void setUpMap() {

        // To show my location button on Google maps
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = MainActivity.locationManager;
        Criteria criteria = new Criteria();

        final Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

    }

    private void addingMarkers() {
        // Adding marker to locations - This is a marker for Shipra Mall, which is close to my house. Zoom out to see the Marker
        double latitude = 28.6322269;
        double longitude = 77.3671697;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Shipra Mall");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        googleMap.addMarker(marker);
        // End - Adding marker to location


        // Another marker for District Center in Janakpuri
        double latitude2 = 28.6292303;
        double longitude2 = 77.0805496;

        MarkerOptions marker2 = new MarkerOptions().position(
                new LatLng(latitude2, longitude2)).title("District Center");
        marker2.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.addMarker(marker2);
        // End - Another marker for District Center in Janakpuri
    }

    private void addingCircles() {
        // Latitude and Longitude of Pacific Mall Subash Nagar
        double latitude = 28.6438947;
        double longitude = 77.1128296;

        // Adding a circle around Pacific Mall of 1000 meters radius.
        googleMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude)) // Setting center point
                .radius(1000) // In meters
                .strokeColor(Color.RED) // Color of Border of Circle
                .fillColor(R.color.semi_blue) // Colour of fill, I've set the color to semi transparent blue which I've defined in colors.xml. For fill colour always use semi transparent colour.
                .strokeWidth(10)); // Width of red border.
    }

    private void setOnLongClickListenerForAddingNewMarkers() {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                // This method will add a new marker to wherever the user long clicks on the Map.
                // The title of the marker will be set based on what is located at that location.
                // The Geocoder.getFromLocation() method gets the address and details of the location.

                // Find Details of location
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert addresses != null;
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                // End - Find details of location

                // Add the new marker to the map
                MarkerOptions mo = new MarkerOptions();
                mo.position(latLng);
                mo.title(knownName + ", " + address + ", " + city + ", " + state + ", " + country);
                mo.icon(BitmapDescriptorFactory
                        .defaultMarker(new Random().nextFloat() * 360));
                googleMap.addMarker(mo);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}

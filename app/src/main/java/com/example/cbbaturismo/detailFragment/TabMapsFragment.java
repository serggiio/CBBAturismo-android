package com.example.cbbaturismo.detailFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.cbbaturismo.R;

import com.example.cbbaturismo.commonService.PermissionUtils;
import com.example.cbbaturismo.commonService.apiService;
import com.example.cbbaturismo.commonService.constantValues;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


public class TabMapsFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    JSONObject jsonData;
    Double latitude, longitude, geoLat, geoLon;
    String markerTitle;
    Marker geoMarker;
    GoogleMap newMap;
    private apiService apiUtil = new apiService();
    JSONObject mapsResponse;
    LocationManager mainLocationManager;
    constantValues constants = new constantValues();
    Bitmap finalImage = null;
    BitmapDescriptor userIcon;

    private String logMap = "TAB MAP GRAGMENT; ";
    private static JSONObject dataMap = new JSONObject();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            newMap = googleMap;
            LatLng sydney = new LatLng(-17.3931978, -66.1560445);
            LatLng positionMarker = new LatLng(latitude, longitude);
            //constants.getApiUrl()+"touristicPlace/mainImage/"+jsonRow.getInt("touristicPlaceId")
            URL url = null;
            Bitmap bmp = null;
            try {
                url = new URL(constants.getApiUrl()+"touristicPlace/mainImage/"+jsonData.getInt("touristicPlaceId"));
                String imageURLBase = constants.getApiUrl()+"touristicPlace/mainImage/"+jsonData.getInt("touristicPlaceId");
                URL imageURL = new URL(imageURLBase);
                URLConnection connection = imageURL.openConnection();
                InputStream iconStream = connection.getInputStream();
                bmp = BitmapFactory.decodeStream(iconStream);
                finalImage = getResizedBitmap(bmp, 140, 140);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            newMap.addMarker(new MarkerOptions().position(positionMarker).title(markerTitle).icon(BitmapDescriptorFactory.fromBitmap(finalImage)));
            newMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionMarker, 18.0f));
            newMap.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.mapStyle)));
            //myLocation();
            if(checkIfLocationOpened()){
                myLocation();
            }

            /*if(geoLat != null ){
                myLocation();
            }*/

        }
    };


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Log.d(logMap, "Parametros recibidos latln: " + getArguments().getString("jsonData"));
        controlPermissions(newMap);
        try {
            /*jsonData = new JSONObject(getArguments().getString("jsonData"));
            latitude = jsonData.getDouble("latitude");
            longitude = jsonData.getDouble("longitude");
            markerTitle = jsonData.getString("placeName");*/

            //jsonData = new JSONObject(getArguments().getString("jsonData"));
            SharedPreferences preferences = this.getActivity().getSharedPreferences("jsonData", Context.MODE_PRIVATE);
            jsonData = new JSONObject(preferences.getString("jsonData", ""));
            Log.d(logMap, "Preference DATA: " + preferences.getString("jsonData", ""));
            Log.d(logMap, "Preference DATA: " + jsonData.getDouble("latitude"));
            Log.d(logMap, "Preference DATA: " + jsonData.getDouble("longitude"));
            Log.d(logMap, "Preference DATA: " + jsonData.getString("placeName"));

            latitude = jsonData.getDouble("latitude");
            longitude = jsonData.getDouble("longitude");
            markerTitle = jsonData.getString("placeName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        View v = inflater.inflate(R.layout.fragment_tab_maps, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }

    @Override
    public void onDetach() {
        //Fragment changed, stop location listener
        //Log.d(logMap, "dettaCHEDDDDDDD: ");

        if(checkIfLocationOpened()){
            mainLocationManager.removeUpdates(locListener);
        }

        super.onDetach();
    }

    private void addMarker(double lat, double lon) {
        LatLng position = new LatLng(lat, lon);
        CameraUpdate myUbication = CameraUpdateFactory.newLatLngZoom(position, 16);
        userIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_user);

        if (geoMarker != null) geoMarker.remove();
        geoMarker = newMap.addMarker(new MarkerOptions()
                .position(position)
                .title("Actual position").icon(userIcon));
        newMap.animateCamera(myUbication);
    }

    private void updateLocation(Location location) {

        LatLng positionMarker = new LatLng(latitude, longitude);
        newMap.addMarker(new MarkerOptions().position(positionMarker).title(markerTitle).icon(BitmapDescriptorFactory.fromBitmap(finalImage)));

        if (location != null) {
            geoLat = location.getLatitude();
            geoLon = location.getLongitude();
            geoLat = -17.371233013830366;
            geoLon = -66.15940006742574;
            addMarker(geoLat, geoLon);

            //call service maps directions
            addPolyline();
            /*Polyline polyline1 = newMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(-17.3931901, -66.1565023),
                            new LatLng(-17.3922601, -66.1566902),
                            new LatLng(-17.3931201, -66.15605789999999)));*/

        }
        //Toast.makeText(getContext(), "CURRENT LOCATION: " + geoLon.toString() + " espacio" + geoLat.toString(), Toast.LENGTH_LONG);
    }

    private boolean checkIfLocationOpened() {

        String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps") || provider.contains("network")){
            return true;
        }
        return false;
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            newMap.clear();
            updateLocation(location);
        }
    };

    private void addPolyline(){

        try{
            JSONObject mapRequest = new JSONObject();
            mapRequest.put("userLat", geoLat);
            mapRequest.put("userLon", geoLon);
            mapRequest.put("placeLat", latitude);
            mapRequest.put("placeLon", longitude);
            mapRequest.put("key", getResources().getString(R.string.google_maps_key));


            mapsResponse = new JSONObject(apiUtil.mapDirections(mapRequest));
            Log.d(logMap, "map RESPONSE: " + mapsResponse);
            JSONArray routeArray = mapsResponse.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = PolyUtil.decode(encodedString);

            Log.d(logMap, "DECODEDDDD: " + list);

            newMap.addPolyline(new PolylineOptions().addAll(list));

        }catch (JSONException e){
            Log.d(logMap, "ERRRORR: " + e.getMessage());
        }
    }


    private void myLocation() {

        //PermissionUtils
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            return;
        }

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateLocation(location);
        mainLocationManager = locationManager;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locListener);
    }

    public void controlPermissions(GoogleMap mMap){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

}
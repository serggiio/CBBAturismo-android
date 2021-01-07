package com.example.cbbaturismo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbbaturismo.adapters.favoriteAdapter;
import com.example.cbbaturismo.adapters.searchMapAdapter;
import com.example.cbbaturismo.commonService.PermissionUtils;
import com.example.cbbaturismo.commonService.apiService;
import com.example.cbbaturismo.commonService.constantValues;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchMapFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    GoogleMap map;
    MapView mapView;
    View mainView;
    private String logSearchMap = "SEARCH MAP FRAGMENT ";
    LocationManager mainLocationManager;
    constantValues constants = new constantValues();
    Bitmap finalImage = null;
    BitmapDescriptor userIcon;
    Marker geoMarker;
    Double latitude, longitude, geoLat, geoLon;
    EditText mapDistance;
    TextView mapDistanceText;
    Button searchButton;
    JSONObject mapRequest;
    JSONObject mapResponse;
    JSONArray adapterList;
    searchMapAdapter mainAdapter;
    JSONObject mapsResponse;
    Boolean routeMarker = false;
    RecyclerView recyclerList;

    private apiService apiUtil = new apiService();

    public Boolean getRouteMarker() {
        return routeMarker;
    }

    public JSONArray getAdapterList() {
        return adapterList;
    }

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
            map = googleMap;
            LatLng sydney = new LatLng(-17.3931978, -66.1560445);
            LatLng positionMarker = sydney;
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setIndoorEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.mapStyle)));
            //map.addMarker(new MarkerOptions().position(positionMarker).title("TITLEE"));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionMarker, 18.0f));
            //listeners();
            myLocation();




        }
    };

    public static SearchMapFragment newInstance(String param1, String param2) {
        SearchMapFragment fragment = new SearchMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_map, container, false);
        mainView = view;
        controlPermissions(map);


        mapView = view.findViewById(R.id.mapView3);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(callback);

        mapDistance = view.findViewById(R.id.distanceMap);
        mapDistanceText = view.findViewById(R.id.textResultMap);
        searchButton = view.findViewById(R.id.btnSearchMap);
        mapDistance.setText("0");

        recyclerList = view.findViewById(R.id.listResultMap);

        listeners();
        //startAdapterService();

        return view;
    }

    private void startAdapterService(){
        try{
            JSONObject data = new JSONObject();
            mapRequest = new JSONObject();
            data.put("latitude", -17.3931978);
            data.put("longitude", -66.1560445);
            data.put("distance", 5);
            mapRequest.put("data", data);
            Log.d(logSearchMap, "request data " + mapRequest);

            mapResponse = new JSONObject(apiUtil.searchByDistance(mapRequest));
            Log.d(logSearchMap, "response data " + mapResponse);
            adapterList = mapResponse.getJSONArray("data");
            mapDistanceText.setText("Resultados: " + adapterList.length());
        }catch (JSONException e){

        }

    }


    private void listeners(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(logSearchMap, "button lsitener +");
                if(!mapDistance.getText().toString().equals("")){
                    int distance = Integer.parseInt(mapDistance.getText().toString());
                    Log.d(logSearchMap, "button lsitener " + distance);

                    //call service

                    JSONObject data = new JSONObject();
                    mapRequest = new JSONObject();
                    try {
                        data.put("latitude", geoLat);
                        data.put("longitude", geoLon);
                        data.put("distance", distance);
                        mapRequest.put("data", data);
                        Log.d(logSearchMap, "request data " + mapRequest);

                        mapResponse = new JSONObject(apiUtil.searchByDistance(mapRequest));
                        Log.d(logSearchMap, "response data " + mapResponse);
                        adapterList = mapResponse.getJSONArray("data");
                        Log.d(logSearchMap, "response array " + adapterList);
                        mapDistanceText.setText("Resultados: " + adapterList.length());

                        addMarkers();
                        //adapterList = null;
                        //mainAdapter.notifyDataSetChanged();

                        updateAdapterList();

                        //excecuteAdapter();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        recyclerList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                View child = recyclerList.findChildViewUnder(e.getX(), e.getY());

                    final int position = recyclerList.getChildAdapterPosition(child);

                    //Log.d(logSearchMap, "route listener.2 " + child);
                    /*if(child != null){

                    }*/

                    getActivity().findViewById(R.id.btnRoute).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Log.d(logSearchMap, "route listener. button " + adapterList.getJSONObject(position));
                                latitude = adapterList.getJSONObject(position).getDouble("latitude");
                                longitude = adapterList.getJSONObject(position).getDouble("longitude");
                                routeMarker = true;
                                map.clear();
                                addMarkers();
                                addMarker(geoLat, geoLon);
                                addPolyline();
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    //return true;
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }



    private void addPolyline(){

        try{
            JSONObject mapRequest = new JSONObject();
            mapRequest.put("userLat", geoLat);
            mapRequest.put("userLon", geoLon);
            mapRequest.put("placeLat", latitude);
            mapRequest.put("placeLon", longitude);
            mapRequest.put("key", getResources().getString(R.string.google_maps_key));


            mapsResponse = new JSONObject(apiUtil.mapDirections(mapRequest));
            Log.d(logSearchMap, "map RESPONSE: " + mapsResponse);
            JSONArray routeArray = mapsResponse.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = PolyUtil.decode(encodedString);

            Log.d(logSearchMap, "DECODEDDDD: " + list);

            map.addPolyline(new PolylineOptions().addAll(list));

        }catch (JSONException e){
            Log.d(logSearchMap, "ERRRORR: " + e.getMessage());
        }
    }

    private void updateAdapterList(){
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.listResultMap);
        recyclerView.setLayoutManager(LayoutManager);
        //Log.d(TAG, "ARRAY JSON " + arrayAdapter);
        //searchMapAdapter adapter = new searchMapAdapter(adapterList, this.getContext(), findNavController(getView()));
        mainAdapter = new searchMapAdapter(adapterList, getContext(), findNavController(getView()));
        recyclerView.setAdapter(mainAdapter);
    }

    private void addMarkers(){
        Log.d(logSearchMap, "List length: " + adapterList.length());
        for(int i = 0; i < adapterList.length(); i++){
            try {
                LatLng tempMarker = new LatLng(adapterList.getJSONObject(i).getDouble("latitude"), adapterList.getJSONObject(i).getDouble("longitude"));
                map.addMarker(new MarkerOptions().position(tempMarker).title(adapterList.getJSONObject(i).getString("placeName")).zIndex(i));
                Log.d(logSearchMap, "marker added: " + adapterList.getJSONObject(i).getString("placeName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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

    public void controlPermissions(GoogleMap mMap){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            Toast.makeText(getContext(), "Es necesario activar la ubicacion", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
            //Navigation.findNavController(getView())
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
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

    private void addMarker(double lat, double lon) {
        LatLng position = new LatLng(lat, lon);
        CameraUpdate myUbication = CameraUpdateFactory.newLatLngZoom(position, 16);
        userIcon = BitmapDescriptorFactory.fromResource(R.drawable.icon_user);

        if (geoMarker != null) geoMarker.remove();
        geoMarker = map.addMarker(new MarkerOptions()
                .position(position)
                .title("Actual position").icon(userIcon));
        map.animateCamera(myUbication);
    }

    private void updateLocation(Location location) {

        //place data
        //LatLng positionMarker = new LatLng(latitude, longitude);
        //map.addMarker(new MarkerOptions().position(positionMarker).title(markerTitle).icon(BitmapDescriptorFactory.fromBitmap(finalImage)));

        if (location != null) {
            geoLat = location.getLatitude();
            geoLon = location.getLongitude();
            geoLat = -17.371233013830366;
            geoLon = -66.15940006742574;
            addMarker(geoLat, geoLon);
            Log.d(logSearchMap, "add route: " + routeMarker);
            if(routeMarker){

                addPolyline();
            }

        }

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
            map.clear();
            if(adapterList != null){
                addMarkers();
            }
            System.out.println("LISTENER ");
            updateLocation(location);
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*LinearLayoutManager LayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.listResultMap);
        recyclerView.setLayoutManager(LayoutManager);
        //Log.d(TAG, "ARRAY JSON " + arrayAdapter);
        //searchMapAdapter adapter = new searchMapAdapter(adapterList, this.getContext(), findNavController(getView()));
        mainAdapter = new searchMapAdapter(adapterList, this.getContext(), findNavController(getView()));
        recyclerView.setAdapter(mainAdapter);*/
    }

    private void excecuteAdapter(){
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.listResultMap);
        recyclerView.setLayoutManager(LayoutManager);
        //Log.d(TAG, "ARRAY JSON " + arrayAdapter);
        searchMapAdapter adapter = new searchMapAdapter(adapterList, this.getContext(), findNavController(getView()));
        recyclerView.setAdapter(adapter);
    }



}
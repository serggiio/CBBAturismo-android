package com.example.cbbaturismo;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cbbaturismo.commonService.apiService;
import com.example.cbbaturismo.commonService.constantValues;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllMapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllMapsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String logAllMaps = "ALL MAPS LOG ";
    private String mParam1;
    private String mParam2;
    private apiService apiUtil = new apiService();
    MapView mapView;
    GoogleMap map;
    constantValues constants = new constantValues();

    ConstraintLayout mapDescriptionLayout;
    TextView placeName;
    RatingBar placeRate;
    ImageView placeImage;
    Button detailButton;
    Toolbar toolbar;

    String apiResponse;
    JSONArray listData;
    JSONObject bundleParams;

    //private OnMapReadyCallback OnMapReadyCallback;


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
            //map.addMarker(new MarkerOptions().position(positionMarker).title("TITLEE"));

            Log.d(logAllMaps, "List length: " + listData.length());
            for(int i = 0; i < listData.length(); i++){
                try {
                    LatLng tempMarker = new LatLng(listData.getJSONObject(i).getDouble("latitude"), listData.getJSONObject(i).getDouble("longitude"));
                    map.addMarker(new MarkerOptions().position(tempMarker).title(listData.getJSONObject(i).getString("placeName")).zIndex(i));
                    Log.d(logAllMaps, "marker added: " + listData.getJSONObject(i).getString("placeName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionMarker, 18.0f));

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.d(logAllMaps, "Marker position: " + marker.getZIndex());
                    int position = Math.round(marker.getZIndex());
                    try {
                        Log.d(logAllMaps, "Marker position: " + listData.get(position));
                        mapDescriptionLayout.setVisibility(View.VISIBLE);
                        JSONObject tempObj = listData.getJSONObject(position);
                        bundleParams = listData.getJSONObject(position);
                        placeName.setText(tempObj.getString("placeName"));
                        placeRate.setProgress(tempObj.getInt("rateAvg"));
                        //TODO: cargar imagen de JSON
                        Picasso.get()
                                .load(constants.getApiUrl()+"touristicPlace/mainImage/"+tempObj.getInt("touristicPlaceId"))
                                //.load("http://192.168.0.15/turismo/public/api/touristicPlace/image")
                                .fit()
                                .placeholder(R.drawable.ic_menu_camera)
                                .into(placeImage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });


        }
    };

    public AllMapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllMapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllMapsFragment newInstance(String param1, String param2) {
        AllMapsFragment fragment = new AllMapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startService();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_maps, container, false);

        mapDescriptionLayout = view.findViewById(R.id.constraintMap);
        placeName = view.findViewById(R.id.mapTitle);
        placeRate = view.findViewById(R.id.mapRate);
        placeImage = view.findViewById(R.id.mapImage);
        detailButton = view.findViewById(R.id.mapButton);
        mapDescriptionLayout.setVisibility(View.GONE);

        toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(callback);

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle param = new Bundle();
                try {
                    param.putInt("findId", bundleParams.getInt("touristicPlaceId"));
                    param.putString("name", bundleParams.getString("placeName"));
                    param.putInt("rate", bundleParams.getInt("rateAvg"));

                    findNavController(view).navigate(R.id.detailFragment, param);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    public void startService(){
        try {
            apiResponse = apiUtil.getAllData();
            Log.d(logAllMaps, "Api response: " + apiResponse);
            JSONObject data = new JSONObject(apiResponse);
            listData = data.getJSONArray("data");

        } catch (JSONException e) {
            Log.d(logAllMaps, "Api response error: " + e.getMessage());
            e.printStackTrace();
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



}
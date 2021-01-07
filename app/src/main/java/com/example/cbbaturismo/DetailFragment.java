package com.example.cbbaturismo;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cbbaturismo.adapters.tabAdapter;
import com.example.cbbaturismo.commonService.apiService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;


public class DetailFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    TabLayout tabContainer;
    TabItem itemStart, itemMap, itemImage, itemVideo;
    private int touristicPlaceId;
    ViewPager viewPager;
    com.example.cbbaturismo.adapters.tabAdapter tabAdapter;
    private apiService apiUtil = new apiService();
    JSONObject apiResponse;
    Toolbar toolbar;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get param ID
        if (getArguments() != null) {
            touristicPlaceId = getArguments().getInt("findId");
            System.out.println("Parametros rebicidos2: "+ getArguments());
            System.out.println("Parametro ID:"  + touristicPlaceId);
        }else{
            Toast.makeText(this.getContext(), "Ocurrio un error", Toast.LENGTH_LONG);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        //instance tab content
        tabContainer = root.findViewById(R.id.tabContainer);
        viewPager = root.findViewById(R.id.viewpager);
        itemStart = root.findViewById(R.id.tabStart);
        itemMap = root.findViewById(R.id.tabMap);
        itemImage = root.findViewById(R.id.tabGalleryImg);
        //itemVideo = root.findViewById(R.id.tabGalleryVideo);

        toolbar = this.getActivity().findViewById(R.id.toolbar);

        toolbar.setTitle("");

        //Call apii service
        try {
            apiResponse = new JSONObject(apiUtil.getDataById(touristicPlaceId));

        } catch (JSONException e) {
            System.out.println("Ocurrio un error que nadie esperaba, va a explotar: " + e);
            e.printStackTrace();
        }
        Log.d("DETAIL TAG", "Respuesta convertida a JSON: " + apiResponse);
        Log.d("DETAIL TAG LOGG", "Respuesta contador: " + tabContainer.getTabCount());
        //ITEM TAB
        //tabAdapter = new tabAdapter(getActivity().getSupportFragmentManager(),tabContainer.getTabCount(), apiResponse);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("jsonData", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferences.edit();
        try {
            objEditor.putString("jsonData", apiResponse.getJSONObject("data").toString());
            objEditor.commit();
            //this.getActivity().finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tabAdapter = new tabAdapter(getChildFragmentManager(),tabContainer.getTabCount(), apiResponse);
        viewPager.setAdapter(tabAdapter);

        tabContainer.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("TAB SELECTEDDD " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    tabAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 1){
                    tabAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 2){
                    tabAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 3){
                    tabAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 4){
                    tabAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabContainer));

        return root;
    }
}
package com.example.cbbaturismo.ui.home;

import com.example.cbbaturismo.adapters.homeAdapter;
import com.example.cbbaturismo.commonService.PermissionUtils;
import com.example.cbbaturismo.commonService.apiService;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.cbbaturismo.R;
import com.example.cbbaturismo.dialogs.GoToDetailFragment;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.Map;

import static androidx.navigation.Navigation.findNavController;


public class HomeFragment extends Fragment {

    ListView tpList;
    ListView listView;
    JSONArray arrayAdapter = new JSONArray();
    GoogleMap newMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private HomeViewModel homeViewModel;
    private apiService apiUtil = new apiService();
    private String apiResponse;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ListView itemView = (ListView) root.findViewById(R.id.tpListView);
        itemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("onItemClick POSITION: "+ i);
                JSONObject itemList = new JSONObject();
                try {
                    itemList = arrayAdapter.getJSONObject(i);
                    Bundle param = new Bundle();
                    param.putInt("findId", itemList.getInt("touristicPlaceId"));
                    param.putString("name", itemList.getString("placeName"));
                    param.putInt("rate", itemList.getInt("rateAvg"));
                    param.putString("gallery", String.valueOf(itemList.getJSONArray("gallery")));

                    System.out.println("GALLERY: " + itemList.getJSONArray("gallery"));
                    System.out.println("Bundle param: " + param);
                    //DONE: Navigate to popup
                    GoToDetailFragment detailController = new GoToDetailFragment(param, findNavController(view));
                    detailController.show(getActivity().getSupportFragmentManager(), "Dialogo fragment");
                    //navigate to detail fragment
                    //findNavController(view).navigate(R.id.detailFragment, param);

                } catch (JSONException e) {
                    System.out.println("Ocurrio un error: " + e);
                    e.printStackTrace();
                }
                System.out.println("END adapter view");

            }
        });

        //final TextView textView = root.findViewById(R.id.textView);
        System.out.println("ANTES DE ASYNC ");

        //Call service
        //apiResponse = apiUtil.getAllTouristicData();
        JSONObject obj;
        JSONArray arrayData = new JSONArray();
        JSONObject obj1;
        try {
            apiResponse = apiUtil.getAllData();
            System.out.println("RESSSS finallllll " );
            System.out.println("DESPUES DE ASYNC " + apiResponse);
            obj = new JSONObject(apiResponse);
            arrayData = obj.getJSONArray("data");
             obj1 = arrayData.getJSONObject(0);
             arrayAdapter = arrayData;
            System.out.println("Resultado array JSON " + arrayData);
            System.out.println("Resultado JSON NORMAL JSON " + obj1);
            System.out.println("Resultado PROPERTY JSON " + obj1.getString("description"));


        }catch (Exception e){
            System.out.println("eRROR encontrado"+ e );
        }
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //tpList = (ListView) getView().findViewById(R.id.tpListView);
        //tpList = (ListView)findViewById(R.id.tpListView);

        //tpList.setAdapter(new homeAdapter(this.getContext(),arrayData));

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);+9
            }
        });

        return root;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //controlPermissions(newMap);
        tpList = (ListView) getView().findViewById(R.id.tpListView);
        //tpList = (ListView)findViewById(R.id.tpListView);

        tpList.setAdapter(new homeAdapter(this.getContext(),arrayAdapter));
    }


}
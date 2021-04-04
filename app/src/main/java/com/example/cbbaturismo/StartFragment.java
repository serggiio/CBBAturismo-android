package com.example.cbbaturismo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cbbaturismo.commonService.PermissionUtils;
import com.example.cbbaturismo.ui.home.HomeFragment;
import com.example.cbbaturismo.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Toolbar toolbar;
    GoogleMap map;
    ImageView cardMap, cardList, cardSearch, cardLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    public StartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
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
        controlPermissions(map);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        //Toast.makeText(getContext().getApplicationContext(), "!Preference DATA " + preferences.getString("userData", ""), Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        final View vista = inflater.inflate(R.layout.fragment_start, container, false);

        cardMap = vista.findViewById(R.id.mapCard);
        cardList = vista.findViewById(R.id.listCard);
        cardSearch = vista.findViewById(R.id.searchCard);
        cardLocation = vista.findViewById(R.id.locationCard);

        //toolbar = vista.findViewById(R.id.toolbar);
        toolbar = this.getActivity().findViewById(R.id.toolbar);

        toolbar.setTitle("");
        //TODO: REVISAR ESTA PARTE
        Activity main = new MainActivity();

        /*
        NavigationView navigationView = vista.findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();*/

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();

        //getActivity().menurecreate();

        System.out.println("USER DATA START FRAGMENT" + preferences.getString("userData", ""));
        if(preferences.getString("userData", "").isEmpty()){
            navMenu.findItem(R.id.profileFragment).setVisible(false);
            navMenu.findItem(R.id.favoriteFragment).setVisible(false);
        }else{
            navMenu.findItem(R.id.loginFragment).setVisible(false);
            navMenu.findItem(R.id.registerFragmentFragment).setVisible(false);
        }



        listeners(vista);

        return vista;
    }

    private void listeners(View view){

        cardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(R.id.allMapsFragment2);
            }
        });

        cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(R.id.nav_home);
            }
        });

        cardSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(R.id.searchFragment);
            }
        });

        cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(R.id.searchMapFragment);
            }
        });
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
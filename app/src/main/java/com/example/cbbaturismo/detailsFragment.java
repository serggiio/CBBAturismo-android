package com.example.cbbaturismo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cbbaturismo.adapters.tabAdapter;
import com.example.cbbaturismo.adapters.tabsAdapter;
import com.example.cbbaturismo.commonService.apiService;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tab1, tab2, tab3;
    tabsAdapter tabsAdapter;
    private int touristicPlaceId;
    private apiService apiUtil = new apiService();
    JSONObject apiResponse;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public detailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment detailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static detailsFragment newInstance(String param1, String param2) {
        detailsFragment fragment = new detailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View root = inflater.inflate(R.layout.fragment_details, container, false);

        tabLayout = root.findViewById(R.id.tabsContainer);
        viewPager = root.findViewById(R.id.tabViewPager);
        tab1 = root.findViewById(R.id.startTab);
        tab2 = root.findViewById(R.id.mapTab);
        tab3 = root.findViewById(R.id.imagesTab);

        //Call apii service
        try {
            apiResponse = new JSONObject(apiUtil.getDataById(touristicPlaceId));

        } catch (JSONException e) {
            System.out.println("Ocurrio un error que nadie esperaba, va a explotar: " + e);
            e.printStackTrace();
        }


        tabsAdapter = new tabsAdapter(getChildFragmentManager(),tabLayout.getTabCount(), apiResponse);
        viewPager.setAdapter(tabsAdapter);

        Log.d("DETAIL TAG", "Respuesta convertida a JSON: " + apiResponse);
        Log.d("DETAIL TAG LOGG", "Respuesta contador: " + tabLayout.getTabCount());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    tabsAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 1){
                    tabsAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 2){
                    tabsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return root;
    }
}
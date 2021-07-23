package com.example.cbbaturismo;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toolbar toolbar;
    ImageSlider imageSlider;
    ImageView cardMap, cardList, cardSearch, cardLocation;
    FloatingActionButton searchButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public newHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static newHomeFragment newInstance(String param1, String param2) {
        newHomeFragment fragment = new newHomeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_home, container, false);

        //hide toolbar
        toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        imageSlider = view.findViewById(R.id.image_slider_home);
        cardMap = view.findViewById(R.id.mapCardNew);
        cardList = view.findViewById(R.id.listCardNew);
        cardLocation = view.findViewById(R.id.locationCardNew);
        searchButton = view.findViewById(R.id.floatingActionButton3);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.header1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.header2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.header3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.header4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.header5, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, null);

        buttonListeners();

        return view;
    }

    private void buttonListeners(){
        cardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("aaaaa", "SADFFFFFFFFF");
                findNavController(view).navigate(R.id.allMapsFragment2);
            }
        });


        cardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(R.id.nav_home);
            }
        });

        cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(R.id.searchMapFragment);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController(view).navigate(R.id.searchFragment);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        imageSlider.stopSliding();
        toolbar.setVisibility(View.VISIBLE);
    }
}
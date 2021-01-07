package com.example.cbbaturismo;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cbbaturismo.adapters.homeAdapter;
import com.example.cbbaturismo.commonService.apiService;
import com.example.cbbaturismo.commonService.constantValues;
import com.example.cbbaturismo.dialogs.GoToDetailFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private apiService apiUtil = new apiService();

    private String logSearch = "SEARCH FRAGMENT ";
    Switch placeEvent;
    RatingBar rate;
    EditText provinceName;
    ImageButton imageButton;
    TextView rateText;
    List<String> listTag = new ArrayList <String>();
    List<String> listChips = new ArrayList <String>();
    JSONArray listTag1;
    JSONObject apiResponse;
    JSONObject searchRequest;
    JSONArray adapterObj;
    ListView searchList;
    ListView itemView;
    Spinner spinerTag;
    ChipGroup tagGroup;
    Chip chipSelected;
    Toolbar toolbar;

    String switchStatus = constantValues.typePlace;

    EditText placeName;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        Log.d(logSearch, "Start search fragment ");
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        placeEvent = view.findViewById(R.id.switchSearch);
        rate = view.findViewById(R.id.rateSearch);
        rateText = view.findViewById(R.id.rateNumber);
        provinceName = view.findViewById(R.id.provinceSearch);
        placeName = view.findViewById(R.id.nameSearch);
        imageButton = view.findViewById(R.id.imageButton);
        searchList = (ListView) view.findViewById(R.id.searchList);
        itemView = (ListView) view.findViewById(R.id.searchList);
        spinerTag = (Spinner) view.findViewById(R.id.spinner);
        tagGroup = view.findViewById(R.id.tagGroup);
        chipSelected = view.findViewById(R.id.chip);

        toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");

        //listTag.add("Hoteles");
        //listTag.add("Plazas");

        JSONObject tagResponse = null;
        try {
            tagResponse = new JSONObject(apiUtil.getTagList(new JSONObject()));
            listTag1 = tagResponse.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(logSearch, "API RESPONSE TAG " + tagResponse);
        Log.d(logSearch, "TAG LIST " + listTag1);

        for (int i=0; i<listTag1.length(); i++) {
            try {
                listTag.add( listTag1.getString(i) );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        eventListeners();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, listTag);
        spinerTag.setAdapter(adapter);




        return view;
    }

    public void eventListeners(){
        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rateText.setText(String.valueOf(rate.getRating()));
            }
        });

        spinerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(logSearch, "TAG ITEM " + listTag.get(i));

                listChips.add(listTag.get(i));

                final Chip chip = new Chip(getContext());
                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(),
                        null,
                        0,
                        R.style.Widget_MaterialComponents_Chip_Entry);
                //chip.setChipDrawable(chipDrawable);
                chip.setText(listTag.get(i));

                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d(logSearch, "chip selected "  + chip.getText());
                        listChips.remove(chip.getText());
                        tagGroup.removeView(chip);
                    }
                });

                tagGroup.addView(chip);

                Log.d(logSearch, "TAG ITEM " + tagGroup.getCheckedChipIds());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tagGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup tagGroup, int checkedId) {
                Chip chip = tagGroup.findViewById(checkedId);

                Log.e("OnCheckedChangeListener", "CHIP: " + chip.getText());
            }
        });

        tagGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("OnCheckedChangeListener", "CHIP: ");
            }
        });



        placeEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    switchStatus = constantValues.typeEvent;
                }else{
                    switchStatus = constantValues.typePlace;
                }
                Log.d(logSearch, "BOOLEAN " + switchStatus);
            }
        });

        //spinerTag.setOnItemClickListener(new );

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(logSearch, "Click on search button ");
                Log.d(logSearch, "BOOLEAN " + switchStatus);
                Log.d(logSearch, "RATE " + rate.getRating());
                Log.d(logSearch, "PROVINCE " + provinceName.getText());
                Log.d(logSearch, "Click on search button " + placeName.getText());

                searchRequest = new JSONObject();
                JSONObject touristicPlace = new JSONObject();
                try {
                    touristicPlace.put("type", switchStatus);
                    touristicPlace.put("name", placeName.getText());
                    touristicPlace.put("province", provinceName.getText());
                    touristicPlace.put("rate", rate.getRating());
                    //touristicPlace.put("tag", listTag);
                    touristicPlace.put("tag", listChips);
                    searchRequest.put("touristicPlace", touristicPlace);

                    //call service
                    apiResponse = new JSONObject(apiUtil.searchData(searchRequest));

                    adapterObj = apiResponse.getJSONArray("data");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(logSearch, "JSON REQUEST " + searchRequest);
                Log.d(logSearch, "JSON RESPONSE " + apiResponse);
                Log.d(logSearch, "JSON ARRAY " + adapterObj);

                searchList.setAdapter(new homeAdapter(getContext(),adapterObj));


            }
        });


        itemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("onItemClick POSITION: "+ i);
                JSONObject itemList = new JSONObject();
                try {
                    itemList = adapterObj.getJSONObject(i);
                    Bundle param = new Bundle();
                    param.putInt("findId", itemList.getInt("touristicPlaceId"));
                    param.putString("name", itemList.getString("placeName"));
                    param.putInt("rate", itemList.getInt("rateAvg"));
                    param.putString("gallery", String.valueOf(itemList.getJSONArray("gallery")));

                    System.out.println("Bundle param: " + param);
                    // Navigate to popup
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
    }
}
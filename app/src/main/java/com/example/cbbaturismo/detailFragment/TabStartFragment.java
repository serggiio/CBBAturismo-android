package com.example.cbbaturismo.detailFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.adapters.buttonAdapter;
import com.example.cbbaturismo.commonService.apiService;
import com.example.cbbaturismo.commonService.constantValues;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.example.cbbaturismo.dialogs.UserRateFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class TabStartFragment extends Fragment {
    Toolbar toolbar;
    ListView buttonList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static JSONObject ARG_PARAMJSON = new JSONObject();

    constantValues constants = new constantValues();
    private String mParam1;
    private String mParam2;
    private JSONObject jsonData;
    private JSONArray tagList;
    private TextView historyText;
    private ImageView mainImage;
    private TextView streets;
    private RatingBar rating;
    private ImageButton fav;
    private FloatingActionButton buttonRate;
    private boolean favoriteStatus;
    private apiService apiUtil = new apiService();
    private JSONObject jsonResponse;
    private Button buttonShare;

    private JSONObject rateRequest;

    public TabStartFragment(JSONObject param) throws JSONException {
        jsonData = param.getJSONObject("data");
        ARG_PARAMJSON = param;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabStartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabStartFragment newInstance(String param1, String param2) throws JSONException {

        TabStartFragment fragment = new TabStartFragment(ARG_PARAMJSON);
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
        View v = inflater.inflate(R.layout.fragment_tab_start, container, false);

        Log.d("TAB FRAGMENT 0", "json recibido11: " + jsonData);

        toolbar = v.findViewById(R.id.toolbarX);
        historyText = v.findViewById(R.id.detailHistory);
        mainImage = v.findViewById(R.id.detailImage);
        rating = v.findViewById(R.id.detailRating);
        streets = v.findViewById(R.id.detailStreets);
        fav = v.findViewById(R.id.detailFavorite);
        buttonRate = v.findViewById(R.id.buttonRate);
        buttonShare = v.findViewById(R.id.shareButton);

        buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAB FRAGMENT 0", "Clicked floating button " );
                UserRateFragment detailController = new UserRateFragment(rateRequest);
                detailController.show(getActivity().getSupportFragmentManager(), "Dialogo fragment");
            }
        });


        try {

            checkFavorite();

            toolbar.setTitle(jsonData.getString("placeName"));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                historyText.setText(Html.fromHtml(jsonData.getString("history"), Html.FROM_HTML_MODE_COMPACT));
            } else {
                historyText.setText(Html.fromHtml(jsonData.getString("history")));
            }

            //historyText.setText(jsonData.getString("history"));
            rating.setProgress(jsonData.getInt("rateAvg"));
            rating.setFocusable(false);
            streets.setText(jsonData.getString("streets"));
            //TODO: cargar imagen de json
            Picasso.get()
                    .load(constants.getApiUrl()+"touristicPlace/mainImage/"+jsonData.getInt("touristicPlaceId"))
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(mainImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAB FRAGMENT 0", "Clicked button " + favoriteStatus);

                JSONObject dataObj = new JSONObject();
                SharedPreferences preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
                JSONObject userData = null;
                try {
                    userData = new JSONObject(preferences.getString("userData", ""));
                    dataObj.put("touristicPlaceId", jsonData.getInt("touristicPlaceId"));
                    dataObj.put("userId", userData.getInt("userId"));


                    if(favoriteStatus){
                        favoriteStatus = false;
                        JSONObject requestData = new JSONObject();
                        requestData.put("action", "delete");
                        requestData.put("data", dataObj);
                        apiUtil.editFavorite(requestData);
                        fav.setImageResource(R.drawable.favorite_opt);

                    }else if(!favoriteStatus){
                        favoriteStatus = true;
                        JSONObject requestData = new JSONObject();
                        requestData.put("action", "save");
                        requestData.put("data", dataObj);
                        apiUtil.editFavorite(requestData);
                        fav.setImageResource(R.drawable.favorite_checked_opt);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAB START", "Clicked SHARE button ");
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareName = null;
                String textLocation = "";
                String uri = "";
                try {
                    shareName = "Ubicacion compartida: " + jsonData.getString("placeName");
                    textLocation = "Direccion: " + jsonData.getString("streets");
                    uri = "http://maps.google.com/maps?saddr=" +jsonData.getDouble("latitude")+","+jsonData.getDouble("longitude");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String shareSub = "Ubicacion compartida";

                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub );
                myIntent.putExtra(Intent.EXTRA_TEXT, shareName);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareName + "\n" + shareSub + "\n" + textLocation + "\n" + uri);
                myIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareName + "\n" + textLocation + "\n" + uri);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });




        return v;
    }

    private void checkFavorite() throws JSONException {

        SharedPreferences preferences = this.getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        //Toast.makeText(getContext().getApplicationContext(), "!Preference DATA " + preferences.getString("userData", ""), Toast.LENGTH_LONG).show();

        if(preferences.getString("userData", "").isEmpty()){
            buttonRate.setVisibility(View.INVISIBLE);
            fav.setVisibility(View.INVISIBLE);
            favoriteStatus = false;
        }else{
            JSONObject request = new JSONObject();
            JSONObject userData = new JSONObject(preferences.getString("userData", ""));
            request.put("touristicPlaceId", jsonData.getInt("touristicPlaceId"));
            request.put("userId", userData.getInt("userId"));
            rateRequest = request;

            Log.d("TAB FRAGMENT 0", "FAVORITE PARAMS " + request);

            jsonResponse = new JSONObject(apiUtil.getFavoriteByUser(request));

            Log.d("TAB FRAGMENT 0", "FAVORITE RESPONSE " + jsonResponse);

            if(jsonResponse.get("data").equals(null)){
                fav.setImageResource(R.drawable.favorite_opt);
                favoriteStatus = false;
            }else{
                //case user favorite place
                fav.setVisibility(View.VISIBLE);
                fav.setImageResource(R.drawable.favorite_checked_opt);
                favoriteStatus = true;
            }

        }



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<String> tNames = null;
        tNames = new ArrayList<String>();
        JSONArray tempList;
        try {
            tempList = jsonData.getJSONArray("tagList");
            for(int i = 0; i<tempList.length();i++){
                tNames.add(tempList.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*tNames.add("Iglesias");
        tNames.add("Plazas");
        tNames.add("Bares");
        tNames.add("Antros");
        tNames.add("Parques");
        tNames.add("Bares2");*/
        Log.d("TAB FRAGMENT 0", "ARRAY LIST TAG: "+ jsonData);
        Log.d("TAB FRAGMENT 0", "ARRAY LIST TAG: "+ tNames);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.tagList);
        recyclerView.setLayoutManager(LayoutManager);
        //RecyclerView.Adapter adapter;
        buttonAdapter adapter = new buttonAdapter(tNames, this.getContext());
        recyclerView.setAdapter(adapter);

    }
}
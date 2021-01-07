package com.example.cbbaturismo.detailFragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.adapters.buttonAdapter;
import com.example.cbbaturismo.adapters.galleryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String logImage = "ImageFragment";

    RecyclerView recyclerView;
    JSONObject jsonData;
    JSONArray jsonArray;
    ImageView galleryImage;

    public TabImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabImageFragment newInstance(String param1, String param2) {
        TabImageFragment fragment = new TabImageFragment();
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

        Log.d(logImage, "Parametros recibidos: " + getArguments().getString("jsonData"));
        try {
            jsonData = new JSONObject(getArguments().getString("jsonData"));
            jsonArray = jsonData.getJSONArray("gallery");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_image, container, false);

        //rec
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) getView().findViewById(R.id.galleryList);
        recyclerView.setLayoutManager(LayoutManager);
        //RecyclerView.Adapter adapter;
        galleryAdapter adapter = new galleryAdapter(jsonArray, this.getContext(), findNavController(getView()));
        recyclerView.setAdapter(adapter);
    }

    public void clickListeners(){

    }
}
package com.example.cbbaturismo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cbbaturismo.adapters.favoriteAdapter;
import com.example.cbbaturismo.commonService.apiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private apiService apiUtil = new apiService();
    private String TAG = "LOG FAV FRAGMENT ";

    private JSONObject jsonResponse;
    private JSONObject favRequest = new JSONObject();
    private JSONArray arrayAdapter;

    Toolbar toolbar;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
        SharedPreferences preferences = this.getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        try {
            JSONObject userData = new JSONObject(preferences.getString("userData", ""));

            favRequest.put("userId", userData.getInt("userId"));
            jsonResponse = new JSONObject(apiUtil.getPlacesByFav(favRequest));
            arrayAdapter = jsonResponse.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.fragment_favorite, container, false);

        toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        LinearLayoutManager LayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.favoriteList);
        recyclerView.setLayoutManager(LayoutManager);
        Log.d(TAG, "ARRAY JSON " + arrayAdapter);
        favoriteAdapter adapter = new favoriteAdapter(arrayAdapter, this.getContext(), findNavController(getView()));
        recyclerView.setAdapter(adapter);
    }
}
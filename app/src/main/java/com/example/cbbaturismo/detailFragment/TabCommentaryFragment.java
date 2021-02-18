package com.example.cbbaturismo.detailFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.adapters.commentaryAdapter;
import com.example.cbbaturismo.adapters.galleryAdapter;
import com.example.cbbaturismo.commonService.apiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabCommentaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabCommentaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String logComment = "COMMENT TAB";
    JSONObject jsonData;
    JSONObject userData;
    JSONObject apiResponse;
    JSONObject apiRequest = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    ImageButton sendButton, deleteButton;
    EditText textCommentary;
    boolean userActive = false;
    View rootView;


    commentaryAdapter adapter;

    int userId, touristicPlaceId;

    RecyclerView recyclerView;

    private apiService apiUtil = new apiService();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabCommentaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabCommentaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabCommentaryFragment newInstance(String param1, String param2) {
        TabCommentaryFragment fragment = new TabCommentaryFragment();
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
        Log.d(logComment, "Parametros recibidos: " + getArguments().getString("jsonData"));
        View v = inflater.inflate(R.layout.fragment_tab_commentary, container, false);
        rootView = v;

        //deleteButton = v.findViewById(R.id.deleteCommentButton);
        sendButton = v.findViewById(R.id.sendCommentaryButton);
        textCommentary = v.findViewById(R.id.editTextCommentaryText);

        //deleteButton.setVisibility(View.INVISIBLE);

        try {

            jsonData = new JSONObject(getArguments().getString("jsonData"));

            JSONObject jsonId = new JSONObject();
            jsonId.put("id", jsonData.getInt("touristicPlaceId"));

            apiRequest.put("touristicPlace", jsonId);
            apiResponse = new JSONObject(apiUtil.getCommentsByTouristicPlaceId(apiRequest));

            jsonArray = apiResponse.getJSONArray("data");

            Log.d(logComment, "RESPUESTA SERVICIO COMENTARIOS: " + apiResponse);

            if(preferences.getString("userData", "").isEmpty()){

                sendButton.setVisibility(View.INVISIBLE);
                textCommentary.setVisibility(View.INVISIBLE);

                userActive = false;

            }else{
                userData = new JSONObject(preferences.getString("userData", ""));
                userActive = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //listeners();


        return v;
    }

    private void listeners() {

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendRequest("save");

            }
        });
    }

    public void sendRequest(String action){
        try {
            JSONObject commentaryRequest = new JSONObject();
            JSONObject dataRequest = new JSONObject();

            commentaryRequest.put("action", action);
            dataRequest.put("userId", userData.get("userId"));
            dataRequest.put("touristicPlaceId", jsonData.get("touristicPlaceId"));
            dataRequest.put("commentaryDesc", String.valueOf(textCommentary.getText()));
            commentaryRequest.put("data", dataRequest);

            apiResponse = new JSONObject(apiUtil.setCommentary(commentaryRequest));

            Log.d(logComment, "ANTES DE EJECUTAR LA LISTA: ");
            Log.d(logComment, "ANTES DE EJECUTAR LA LISTA2: " + jsonArray);
            jsonArray = new JSONArray();
            adapter.notifyDataSetChanged();
            jsonArray = apiResponse.getJSONArray("data");

            adapter = new commentaryAdapter(jsonArray, userActive, rootView, this.getActivity());
            recyclerView.setAdapter(adapter);

            /*recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
            adapter.notifyDataSetChanged();*/
            //adapter.notifyDataSetChanged();
            Log.d(logComment, "DESPUES DE EJECUTAR LA LISTA ");
            //PROBAR CREANDO NUEVAMENTE EL ADAPTADOR

            //adapter.notify();
            //adapter.


            /*commentaryAdapter adapter = new commentaryAdapter(jsonArray);
            recyclerView.setAdapter(adapter);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) getView().findViewById(R.id.commentaryList);
        recyclerView.setLayoutManager(LayoutManager);
        //RecyclerView.Adapter adapter;
        adapter = new commentaryAdapter(jsonArray, userActive, rootView, this.getActivity());
        recyclerView.setAdapter(adapter);

        listeners();
    }
}
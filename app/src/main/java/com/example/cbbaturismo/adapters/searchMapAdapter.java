package com.example.cbbaturismo.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.SearchMapFragment;
import com.example.cbbaturismo.commonService.apiService;
import com.example.cbbaturismo.commonService.constantValues;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.navigation.Navigation.findNavController;

public class searchMapAdapter extends RecyclerView.Adapter<searchMapAdapter.ViewHolder>{

    private static final String TAG = "searchMap RecyclerView ";
    constantValues constants = new constantValues();
    //vars
    private JSONArray jsonData;
    private JSONObject rowData;
    private Context tContext;
    private apiService apiUtil = new apiService();
    NavController navController;
    SearchMapFragment mainClass = new SearchMapFragment();


    public searchMapAdapter(JSONArray data, Context tContext, NavController navController) {
        this.navController = navController;
        this.jsonData = data;
        this.tContext = tContext;
    }

    @NonNull
    @Override
    public searchMapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_item_list, parent, false);
        return new searchMapAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull searchMapAdapter.ViewHolder holder, final int position) {
        //asignar datos layout
        Log.d(TAG, "onBindViewHolder: called."+ jsonData);
        try {
            rowData = jsonData.getJSONObject(position);


            Picasso.get()
                    .load(constants.getApiUrl()+"touristicPlace/mainImage/"+rowData.getInt("touristicPlaceId"))
                    //.load(constants.getApiUrl()+ "touristicPlace/image")
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(holder.mapImage);

            holder.mapName.setText(rowData.getString("placeName"));
            holder.mapDistance.setText("Aproximado " + String.valueOf(rowData.getInt("distance")) + " m.");

            holder.btnVisit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle param = new Bundle();
                    try {
                        Log.d(TAG, "click listener. "+ jsonData.getJSONObject(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        param.putInt("findId", jsonData.getJSONObject(position).getInt("touristicPlaceId"));
                        param.putString("name", jsonData.getJSONObject(position).getString("placeName"));
                        param.putInt("rate", jsonData.getJSONObject(position).getInt("rateAvg"));

                        findNavController(view).navigate(R.id.detailFragment, param);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.btnRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*try {

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*///ESTABLECER DATO DE PREFERENCIA

                    Log.d(TAG, "route listener. ");
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonData.length();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mapImage;
        TextView mapName, mapDistance;
        Button btnVisit, btnRoute;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mapImage = itemView.findViewById(R.id.mapSearchImage);
            mapName = itemView.findViewById(R.id.searchMapTitle);
            mapDistance = itemView.findViewById(R.id.searchMapDistance);
            btnVisit = itemView.findViewById(R.id.btnVisit);
            btnRoute = itemView.findViewById(R.id.btnRoute);
            //mapName = itemView.findViewById(R.id.)

        }
    }

}

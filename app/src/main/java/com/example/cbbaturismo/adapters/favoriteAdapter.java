package com.example.cbbaturismo.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.apiService;
import com.example.cbbaturismo.commonService.constantValues;
import com.squareup.picasso.Picasso;
//import com.example.cbbaturismo.commonService.GlideApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class favoriteAdapter extends RecyclerView.Adapter<favoriteAdapter.ViewHolder> {

    private static final String TAG = "favorite RecyclerView ";
    constantValues constants = new constantValues();
    //vars
    private JSONArray jsonData;
    private JSONObject rowData;
    private Context tContext;
    private apiService apiUtil = new apiService();
    NavController navController;


    public favoriteAdapter(JSONArray data, Context tContext, NavController navController) {
        this.navController = navController;
        this.jsonData = data;
        this.tContext = tContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //asignar datos layout
        Log.d(TAG, "onBindViewHolder: called.");
        try {
            rowData = jsonData.getJSONObject(position);


            Picasso.get()
                    .load(constants.getApiUrl()+"touristicPlace/mainImage/"+rowData.getInt("touristicPlaceId"))
                    //.load(constants.getApiUrl()+ "touristicPlace/image")
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(holder.favoriteImage);

            holder.favoriteName.setText(rowData.getJSONObject("touristic_place").getString("placeName"));

            holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        JSONObject dataObj = new JSONObject();
                        SharedPreferences preferences = tContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
                        JSONObject userData = null;
                        userData = new JSONObject(preferences.getString("userData", ""));
                        dataObj.put("touristicPlaceId", jsonData.getJSONObject(position).getInt("touristicPlaceId"));
                        dataObj.put("userId", userData.getInt("userId"));

                        Log.d(TAG, "onClick clicked on button: "+ jsonData.getJSONObject(position));

                        jsonData.remove(position);

                        JSONObject requestData = new JSONObject();
                        requestData.put("action", "delete");
                        requestData.put("data", dataObj);
                        apiUtil.editFavorite(requestData);

                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {



                        Log.d(TAG, "onClick clicked on button: "+ jsonData.getJSONObject(position));

                        Bundle param = new Bundle();
                        param.putInt("findId", jsonData.getJSONObject(position).getInt("touristicPlaceId"));
                        param.putString("name", jsonData.getJSONObject(position).getJSONObject("touristic_place").getString("placeName"));
                        param.putInt("rate", 0);
                        navController.navigate(R.id.detailFragment, param);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        CircleImageView favoriteImage;
        TextView favoriteName;
        ImageButton favoriteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favoriteImage = itemView.findViewById(R.id.favoriteImage);
            favoriteName = itemView.findViewById(R.id.favoriteName);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

        }
    }
}

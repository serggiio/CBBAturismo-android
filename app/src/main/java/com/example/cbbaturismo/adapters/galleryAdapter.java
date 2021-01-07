package com.example.cbbaturismo.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.constantValues;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class galleryAdapter extends RecyclerView.Adapter<galleryAdapter.ViewHolder>{

    private static final String TAG = "AdapterGallery";
    constantValues constants = new constantValues();
    //vars
    JSONArray galleryItems;
    private Context tContext;
    String galleryTitle, galleryPath;
    View view;
    NavController navController;
    Integer imageId;

    public galleryAdapter(JSONArray galleryItems, Context tContext, NavController navController) {
        this.navController = navController;
        this.galleryItems = galleryItems;
        this.tContext = tContext;
    }

    @NonNull
    @Override
    public galleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //retornar cada elemento del array en layout

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_card_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final galleryAdapter.ViewHolder holder, final int position) {
        //asignar datos del layout
        //Log.d(TAG, "onBindViewHolder: called." + galleryItems);

        JSONObject jsonRow = new JSONObject();
        try {
            jsonRow = galleryItems.getJSONObject(position);
            imageId = jsonRow.getJSONArray("images").getJSONObject(0).getInt("imageId");
            galleryTitle = jsonRow.getString("galleryName");
            galleryPath = jsonRow.getJSONArray("images").getJSONObject(0).getString("imagePath");
        } catch (JSONException e) {
            Log.d(TAG, "onBindViewHolder: called. ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        Log.d(TAG, "JSON ROW: " + jsonRow);

        Picasso.get()
                .load(constants.getApiUrl()+"touristicPlace/image/" + imageId)
                .placeholder(R.drawable.ic_menu_camera)
                .into(holder.image);

        holder.galleryName.setText(galleryTitle);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject rowJson = galleryItems.getJSONObject(position);
                    JSONArray images = rowJson.getJSONArray("images");
                    Log.d(TAG, "onClick clicked on image: "+ rowJson);
                    Log.d(TAG, "onClick params sent: "+ images);
                    //findNavController(view).navigate(R.id.);
                    Bundle params = new Bundle();
                    params.putString("jsonData", images.toString());
                    navController.navigate(R.id.galleryItemsFragment, params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryItems.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView galleryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.galleryImage);
            galleryName = itemView.findViewById(R.id.galleryText);
        }
    }
}

package com.example.cbbaturismo.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.navigation.NavController;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.constantValues;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class gridAdapter extends BaseAdapter {
    private Context context;
    private JSONArray imagesData;
    NavController navController;
    String imagePath;
    constantValues constants = new constantValues();

    public gridAdapter(Context context, JSONArray imagesData, NavController navController) {
        this.context = context;
        this.imagesData = imagesData;
        this.navController = navController;
    }

    @Override
    public int getCount() {
        return imagesData.length();
    }

    @Override
    public Object getItem(int i) {
        Object jsonObject = null;
        try {
            jsonObject = imagesData.get(i);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        try {
            imagePath = constants.getApiUrl()+"touristicPlace/image/" + imagesData.getJSONObject(i).getInt("imageId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //imagePath = imagesData.getJSONObject(i).getString("imagePath");

        Picasso.get()
                //.load(constants.getApiUrl()+"touristicPlace/image")
                .load(imagePath)
                .fit()
                .placeholder(R.drawable.ic_menu_camera)
                .into(imageView);

        imageView.setLayoutParams(
                new GridView.LayoutParams(
                        340,
                        350
                )
        );

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject rowData = null;
                try {
                    rowData = imagesData.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("IMAGE DETAIL", "Se clickeo una iamgen" + rowData);
                Bundle params = new Bundle();
                try {
                    params.putString("imagePath", String.valueOf(constants.getApiUrl()+"touristicPlace/image/" + rowData.getInt("imageId")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                navController.navigate(R.id.fullImageFragment, params);
            }
        });

        return imageView;
    }
}

package com.example.cbbaturismo.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.constantValues;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class homeAdapter extends BaseAdapter {

    //layoutInflater instanciar diseño de itemsxml
    private static LayoutInflater inflater = null;
    Context context;
    JSONArray dataList;
    constantValues constants = new constantValues();

    public homeAdapter(Context context, JSONArray dataList) {
        System.out.println("Start HOME ADAPTER");
        this.context = context;
        this.dataList = dataList;
        //layout inflate service instanciar xml de items
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        //declarar elementos, inicializar y asignar valores
        System.out.println("START GET VIEWWWWWW " );
        final View vista = inflater.inflate(R.layout.tp_element_list, null);
        JSONObject jsonRow = new JSONObject();

        try {
            jsonRow = dataList.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("ROWWWW " + jsonRow);
        System.out.println("TEST VALUE " );
        TextView title = (TextView) vista.findViewById(R.id.itemTitle);
        TextView province = (TextView) vista.findViewById(R.id.itemProvince);
        ImageView itemImage = (ImageView) vista.findViewById(R.id.itemImage);
        RatingBar rate = (RatingBar) vista.findViewById(R.id.itemRating);

        try {
            title.setText(jsonRow.getString("placeName"));
            province.setText(jsonRow.getString("provinceName"));
            //itemImage.setImageResource();
            rate.setProgress(jsonRow.getInt("rateAvg"));
            //TODO: cargar imagen de json
            Picasso.get()
                    //.load(constants.getApiUrl()+"touristicPlace/image")
                    .load(constants.getApiUrl()+"touristicPlace/mainImage/"+jsonRow.getInt("touristicPlaceId"))
                    .fit()
                    .placeholder(R.drawable.ic_menu_camera)
                    .into(itemImage);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error ENCONTRADO EN: " + e);
        };
        return vista;
    }

    @Override
    public int getCount() {
        return dataList.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}

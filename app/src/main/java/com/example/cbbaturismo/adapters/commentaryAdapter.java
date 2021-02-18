package com.example.cbbaturismo.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.apiService;
import com.example.cbbaturismo.commonService.constantValues;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class commentaryAdapter extends RecyclerView.Adapter<commentaryAdapter.ViewHolder> {

    private static final String TAG = "AdapterCommentary";
    constantValues constants = new constantValues();
    //vars
    JSONArray comentaryItems;
    private Context tContext;
    View view;
    NavController navController;
    TextView commentaryDescription, userData;
    String commentaryString, userString;
    boolean status;
    View rootView;
    //ImageButton deleteButton;
    boolean selectedStatus = false;
    int selectedPosition;
    int defaultColor, actualColor;
    ImageButton deleteBtn;
    FragmentActivity fragmentActivity;
    JSONObject userDataJson;
    private apiService apiUtil = new apiService();


    public commentaryAdapter(JSONArray galleryItems, boolean status, View rootView, FragmentActivity fragmentActivity) {
        this.navController = navController;
        this.comentaryItems = galleryItems;
        this.tContext = tContext;
        this.status = status;
        this.rootView = rootView;
        this.fragmentActivity = fragmentActivity;

    }

    @NonNull
    @Override
    public commentaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //retornar cada elemento del array en layout

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commentary_iem_list, parent, false);
        return new commentaryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final commentaryAdapter.ViewHolder holder, final int position) {
        //asignar datos del layout
        //Log.d(TAG, "onBindViewHolder: called." + galleryItems);
        SharedPreferences preferences = fragmentActivity.getSharedPreferences("userData", Context.MODE_PRIVATE);

        JSONObject jsonRow = new JSONObject();
        //deleteButton = rootView.findViewById(R.id.deleteCommentButton);

        try {

            jsonRow = comentaryItems.getJSONObject(position);
            commentaryString = jsonRow.getString("commentaryDesc");
            userString = jsonRow.getJSONObject("user").getString("name") + " " + jsonRow.getJSONObject("user").getString("lastName") + " " + jsonRow.getString("created_at");

        } catch (JSONException e) {
            Log.d(TAG, "onBindViewHolder: called. ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        Log.d(TAG, "JSON ROW: " + jsonRow);

        commentaryDescription.setText(commentaryString);
        userData.setText(userString);

        if(preferences.getString("userData", "").isEmpty()){
            deleteBtn.setVisibility(View.INVISIBLE);
        }else{

            try {

                userDataJson = new JSONObject(preferences.getString("userData", ""));
                //Log.d(TAG, "COMPARANDO: " + userDataJson.getInt("userId") + "/////" + jsonRow.getInt("userId"));
                if(userDataJson.getInt("userId") == jsonRow.getInt("userId")){
                    //Log.d(TAG, "COMPARANDO: " + "Entro al if");
                    deleteBtn.setVisibility(View.VISIBLE);
                }else{
                    deleteBtn.setVisibility(View.INVISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //card default color
        defaultColor = Color.parseColor("#E6E6E6");
        selectedStatus = false;

        holder.cardItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /*if(status){

                    try {

                        actualColor = holder.cardItem.getCardBackgroundColor().getDefaultColor();
                        //Log.d(TAG, "LONG CLICK CARD COLOR: " + actualColor);
                        //Log.d(TAG, "LONG CLICK CARD COLOR OLD: " + oldColor);
                        Log.d(TAG, "COLOR ESTADO: " +  selectedStatus);

                        if(defaultColor == actualColor && !selectedStatus) {

                            rootView.findViewById(R.id.cardCommentary).setBackgroundColor(Color.CYAN);
                            Log.d(TAG, "PRIMER IF CAMBIA A CYAN: " );
                            holder.cardItem.setCardBackgroundColor(Color.CYAN);
                            //deleteButton.setVisibility(View.VISIBLE);
                            selectedStatus = true;
                            selectedPosition = position;

                        }else if(defaultColor != actualColor){

                            Log.d(TAG, "SEGUNDO IF CAMBIA A NORMAL: ");
                            holder.cardItem.setCardBackgroundColor(defaultColor);
                            //deleteButton.setVisibility(View.INVISIBLE);
                            selectedStatus = false;
                            selectedPosition = -1;
                        }

                        //Log.d(TAG, "LONG CLICK CARD: " + comentaryItems.getJSONObject(position));

                        Log.d(TAG, "POSICION: " +  selectedPosition);
                        if(selectedPosition != -1){
                            Log.d(TAG, "DATA: " +  comentaryItems.getJSONObject(selectedPosition));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }*/

                return false;
            }
        });

        /*deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition != -1){
                    try {

                        Log.d(TAG, "se eliminara la posicion: " +  selectedPosition);
                        Log.d(TAG, "se eliminara el elemento: " +  comentaryItems.getJSONObject(selectedPosition));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });*/

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "se eliminara el elemento: " +  comentaryItems.getJSONObject(position));
                    JSONObject deleteRequest = new JSONObject();
                    deleteRequest.put("action", "delete");

                    JSONObject dataObj = new JSONObject();
                    dataObj.put("userId", userDataJson.getInt("userId"));
                    dataObj.put("commentaryId", comentaryItems.getJSONObject(position).get("commentaryId"));
                    dataObj.put("touristicPlaceId", comentaryItems.getJSONObject(position).get("touristicPlaceId"));

                    deleteRequest.put("data", dataObj);

                    JSONObject apiResponse = new JSONObject(apiUtil.setCommentary(deleteRequest));

                    comentaryItems = apiResponse.getJSONArray("data");
                    notifyItemRemoved(position);
                    //notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return comentaryItems.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentaryDescription = itemView.findViewById(R.id.textViewCommentary);
            userData = itemView.findViewById(R.id.textViewUserDate);
            cardItem = itemView.findViewById(R.id.cardCommentary);
            deleteBtn = itemView.findViewById(R.id.deleteCommentBtn);
        }
    }
}

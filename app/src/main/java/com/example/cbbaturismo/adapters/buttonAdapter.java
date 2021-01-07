package com.example.cbbaturismo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.constantValues;
import com.squareup.picasso.Picasso;
//import com.example.cbbaturismo.commonService.GlideApp;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class buttonAdapter extends RecyclerView.Adapter<buttonAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    constantValues constants = new constantValues();
    //vars
    private ArrayList<String> tNames = new ArrayList<>();
    private Context tContext;

    public buttonAdapter(ArrayList<String> tNames, Context tContext) {
        this.tNames = tNames;
        this.tContext = tContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //retornar cada elemento del array en layout

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_button_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //asignar datos del layout
        Log.d(TAG, "onBindViewHolder: called.");

        Picasso.get()
                .load(constants.getApiUrl()+ constants.tagImage+tNames.get(position).toLowerCase())
                .placeholder(R.drawable.ic_menu_camera)
                .into(holder.image);

        holder.tagName.setText(tNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick clicked on image: "+ tNames.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView tagName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.circleImage);
            tagName = itemView.findViewById(R.id.tagName);
        }
    }



}

package com.example.cbbaturismo.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.constantValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




public class GoToDetailFragment extends DialogFragment {

    ImageButton btnQuit;
    Button detailButton;
    LinearLayout superiorBar;
    ImageView image1;
    ImageSlider imageSlider;
    Bundle param;
    NavController navController;
    TextView sliderName;
    RatingBar sliderRating;
    JSONArray gallery;

    constantValues constants = new constantValues();


    public GoToDetailFragment(Bundle param, NavController navController) throws JSONException {
        // Required empty public constructor
        this.param = param;
        System.out.println("PARAMSSS "+ this.param);
        this.navController = navController;
        this.gallery = new JSONArray(param.getString("gallery"));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialogDetail();
    }

    private Dialog createDialogDetail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_go_to_detail, null);
        builder.setView(v);
        superiorBar = v.findViewById(R.id.barraSuperiorId);
        btnQuit = v.findViewById(R.id.btnSalir);
        detailButton = v.findViewById(R.id.btnGoTo);
        sliderName = v.findViewById(R.id.sliderText);
        sliderRating = v.findViewById(R.id.sliderRating);
        imageSlider = v.findViewById(R.id.image_slider);

        System.out.println("GALLERY DATA" + gallery);

        List<SlideModel> slideModels = new ArrayList<>();

        if(gallery.isNull(0)){
            slideModels.add(new SlideModel(constants.getApiUrl()+"touristicPlace/mainImage/"+param.getInt("findId"), ScaleTypes.FIT));

        }else{
            //slideModels.add(new SlideModel(constants.getApiUrl()+"touristicPlace/image", ScaleTypes.FIT));
            slideModels.add(new SlideModel(constants.getApiUrl()+"touristicPlace/mainImage/"+param.getInt("findId"), ScaleTypes.FIT));
            try{
                JSONObject galleryObj = gallery.getJSONObject(0);

                for (int i = 0; i < galleryObj.getJSONArray("images").length(); i++) {
                    System.out.println("IDSSS " + galleryObj.getJSONArray("images").getJSONObject(i).getInt("imageId"));
                    slideModels.add(new SlideModel(constants.getApiUrl()+"touristicPlace/image/" + galleryObj.getJSONArray("images").getJSONObject(i).getInt("imageId"), ScaleTypes.FIT));
                }
            } catch (JSONException e) {
                System.out.println("Ocurrio un error: " + e);
                e.printStackTrace();
            }
        }


        imageSlider.setImageList(slideModels, null);
        sliderName.setText(param.getString("name"));
        sliderRating.setProgress(param.getInt("rate"));

        //image1 = v.findViewById(R.id.image1);

        //image1.setImageResource(R.drawable.ic_menu_gallery);

        buttonsEvents();
        System.out.println("SHEGO HASTA ACA3");
        return builder.create();

    }

    private void buttonsEvents() {
    btnQuit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            imageSlider.stopSliding();
            dismiss();
        }
    });
    detailButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
            imageSlider.stopSliding();
            navController.navigate(R.id.detailFragment, param);
            //navController.navigate(R.id.detailsFragment2, param);

        }
    });

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){

        }else{
            throw new RuntimeException(context.toString()+"aaaaaaa");
        }
    }


    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_go_to_detail, container, false);
    }*/
}
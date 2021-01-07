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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.apiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class UserRateFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private JSONObject params = new JSONObject();
    ImageButton btnQuit;
    RatingBar rateBar;
    TextView userRate;
    private apiService apiUtil = new apiService();
    private JSONObject rateResponse;
    private String logDialogRate = "DIALOG RATE FRAGMENT ";
    private int selectedRate;

    public UserRateFragment(JSONObject request) {
        params = request;
        // Required empty public constructor
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof Activity){

        }else{
            throw new RuntimeException(context.toString()+"aaaaaaa");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return createDialogRate();

    }

    private Dialog createDialogRate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_user_rate, null);

        btnQuit = v.findViewById(R.id.btnSalirRate);
        rateBar = v.findViewById(R.id.userRatingBar);
        userRate = v.findViewById(R.id.userRatingDesc);
            try {
            getRateByUser(false);
        }catch (JSONException e){

        }

        rateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.d(logDialogRate, "BAR LISTENER " + v + b);
                selectedRate = Math.round(v);
                try {
                    getRateByUser(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setView(v);

        return builder.create();
    }

    private void getRateByUser(Boolean saveRate) throws JSONException {
        Log.d(logDialogRate, "PARAMS " + params);
        if(saveRate){
            params.put("puntuacion", selectedRate);
        }
        rateResponse = new JSONObject(apiUtil.getRateByUser(params));
        Log.d(logDialogRate, "RESPONSE " + rateResponse);
        //Log.d(logDialogRate, "INT " + rateResponse.getInt("puntuacion"));
        if(!rateResponse.getJSONObject("data").equals(null)){
            JSONObject temp = rateResponse.getJSONObject("data");
            rateBar.setProgress(temp.getInt("puntuacion"));
            userRate.setText(String.valueOf(temp.getInt("puntuacion")));
        }

    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_rate, container, false);
    }*/
}
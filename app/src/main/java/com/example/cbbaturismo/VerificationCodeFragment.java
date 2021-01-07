package com.example.cbbaturismo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cbbaturismo.commonService.apiService;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificationCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificationCodeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String email;
    private apiService apiUtil = new apiService();
    private String logVerification = "FRAGMENT VERIFICATION ";

    EditText textCode;
    TextView textEmail, textMessage;
    Button saveButton;
    NavigationView navigationView;
    Menu navMenu;

    public VerificationCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerificationCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificationCodeFragment newInstance(String param1, String param2) {
        VerificationCodeFragment fragment = new VerificationCodeFragment();
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
            email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verification_code, container, false);


        textCode = view.findViewById(R.id.verificationCode);
        textEmail = view.findViewById(R.id.verificationEmail);
        textMessage = view.findViewById(R.id.verificationMessage);
        saveButton = view.findViewById(R.id.verificationButton);

        navigationView = getActivity().findViewById(R.id.nav_view);
        navMenu = navigationView.getMenu();

        textEmail.setText(email);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(textCode.getText()).isEmpty()){
                    textCode.setError(getResources().getString(R.string.verificationInvalidCode));
                }else{

                    try {
                        verificateCode();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });



        return view;
    }

    public void verificateCode() throws JSONException {

        JSONObject verificationRequest = new JSONObject();
        JSONObject verificationResponse;

        verificationRequest.put("email", email);
        verificationRequest.put("verificationCode", String.valueOf(textCode.getText()));

        verificationResponse = new JSONObject(apiUtil.verificateUserCode(verificationRequest));

        Log.d(logVerification, "Api response: " + verificationResponse);
        Log.d(logVerification, "Api response code: " + verificationResponse.getString("code"));

        if(verificationResponse.getString("code").equals("Error") ){
            Log.d(logVerification, "CODE ERROR: " + verificationResponse.getString("code"));
            textMessage.setText(R.string.verificationInvalidCode);
        }else if(verificationResponse.getString("code").equals("OK")){
            Log.d(logVerification, "CODE OK: " + verificationResponse.getString("code"));
            //set login data as preferences
            SharedPreferences preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
            SharedPreferences.Editor objEditor = preferences.edit();
            objEditor.putString("userData", verificationResponse.getJSONObject("data").toString());
            objEditor.apply();

            navMenu.findItem(R.id.loginFragment).setVisible(false);
            navMenu.findItem(R.id.registerFragmentFragment).setVisible(false);

            navMenu.findItem(R.id.profileFragment).setVisible(true);
            navMenu.findItem(R.id.favoriteFragment).setVisible(true);

            findNavController(getView()).navigate(R.id.ConstraintLayout);

        }
    }
}
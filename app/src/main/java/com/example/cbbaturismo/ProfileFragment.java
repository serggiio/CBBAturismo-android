package com.example.cbbaturismo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbbaturismo.commonService.apiService;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.cbbaturismo.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String logProfile = "PROFILE FRAGMENT ";
    private JSONObject userData;
    private apiService apiUtil = new apiService();
    private JSONObject apiResponse;
    private JSONObject apiRequest = new JSONObject();

    EditText name, lastName, email;
    TextView status;
    Button save, logOut;
    Switch editable;

    NavigationView navigationView;
    Menu navMenu;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.profileName);
        lastName = view.findViewById(R.id.profileLastName);
        email = view.findViewById(R.id.profileEmail);
        status = view.findViewById(R.id.profileStatus);
        editable = view.findViewById(R.id.profileEditable);
        save = view.findViewById(R.id.profileSave);
        logOut = view.findViewById(R.id.profileClose);

        navigationView = getActivity().findViewById(R.id.nav_view);
        navMenu = navigationView.getMenu();

        SharedPreferences preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
        Log.d(logProfile, "USER DATA" + preferences.getString("userData", ""));
        try {
            userData = new JSONObject(preferences.getString("userData", ""));
            initFragment();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

    private void initFragment() throws JSONException {
        name.setText(userData.getString("name"));
        lastName.setText(userData.getString("lastName"));
        email.setText(userData.getString("email"));

        name.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        save.setEnabled(false);

        if(userData.getInt("statusId") == 2){
            status.setText("ACTIVO");
        }else{
            status.setText("INACTIVO");
        }

        eventListeners();

    }

    private void eventListeners(){

        editable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    name.setEnabled(true);
                    lastName.setEnabled(true);
                    email.setEnabled(true);
                    save.setEnabled(true);
                }else{
                    name.setEnabled(false);
                    lastName.setEnabled(false);
                    email.setEnabled(false);
                    save.setEnabled(false);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(logProfile, "BUTTON PRESSED SAVE");
                try {
                    apiRequest.put("id", userData.getInt("userId"));
                    JSONObject tempData = new JSONObject();
                    tempData.put("name", String.valueOf(name.getText()));
                    tempData.put("lastName", String.valueOf(lastName.getText()));
                    tempData.put("email", String.valueOf(email.getText()));
                    apiRequest.put("data", tempData);

                    apiResponse = new JSONObject(apiUtil.updateUser(apiRequest));

                    Log.d(logProfile, "EVENT UPDATE RESULT "+ apiResponse);

                    if(apiResponse.getString("code").equals("OK")){
                        Log.d(logProfile, "UPDATE SUCCESSS ");
                        Toast.makeText(getContext().getApplicationContext(), getActivity().getText(R.string.profileUpdateSuccess), Toast.LENGTH_LONG).show();

                        SharedPreferences preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor objEditor = preferences.edit();
                        objEditor.putString("userData", apiResponse.getJSONObject("data").toString());
                        objEditor.apply();

                        userData = new JSONObject(preferences.getString("userData", ""));
                        initFragment();
                        //findNavController(view).navigate(R.id.ConstraintLayout);
                    }else{
                        Log.d(logProfile, "UPDATE FAILED ");
                        Toast.makeText(getContext().getApplicationContext(), getActivity().getText(R.string.profileUpdateFail), Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(logProfile, "BUTTON PRESSED LOGOUT");

                    Toast.makeText(getContext().getApplicationContext(), getActivity().getText(R.string.profileLogoutMessage), Toast.LENGTH_LONG).show();

                    SharedPreferences preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor objEditor = preferences.edit();
                    objEditor.putString("userData", null);
                    objEditor.apply();

                    navMenu.findItem(R.id.loginFragment).setVisible(true);
                    navMenu.findItem(R.id.registerFragmentFragment).setVisible(true);

                    navMenu.findItem(R.id.profileFragment).setVisible(false);
                    navMenu.findItem(R.id.favoriteFragment).setVisible(false);

                findNavController(view).navigate(R.id.ConstraintLayout);
            }
        });
    }

}
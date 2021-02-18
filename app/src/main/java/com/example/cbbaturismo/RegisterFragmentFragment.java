package com.example.cbbaturismo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbbaturismo.commonService.apiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private apiService apiUtil = new apiService();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String logRegister = "REGISTER FRAGMENT ";
    EditText textName;
    EditText textLastName;
    EditText textEmail;
    EditText textPassword;
    TextView textAlert;
    CheckBox showPassword;
    Button saveButton;
    JSONObject saveObj = new JSONObject();
    JSONObject requestSave = new JSONObject();
    JSONObject apiResponse;
    View viewController;

    public RegisterFragmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragmentFragment newInstance(String param1, String param2) {
        RegisterFragmentFragment fragment = new RegisterFragmentFragment();
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
        View view = inflater.inflate(R.layout.fragment_register_fragment, container, false);
        viewController = view;
        textName = view.findViewById(R.id.registerName);
        textLastName = view.findViewById(R.id.registerLastName);
        textEmail = view.findViewById(R.id.registerEmail);
        textPassword = view.findViewById(R.id.registerPassword);
        showPassword = view.findViewById(R.id.showPassword);
        textAlert = view.findViewById(R.id.registerAlertText);
        saveButton = view.findViewById(R.id.registerButton);


        eventListeners();

        //validateAndSave();

        return view;
    }

    private void validateAndSave() throws JSONException {

        Log.d(logRegister, "Name: " + textName.getText());
        Log.d(logRegister, "Last Name: " + textLastName.getText());
        Log.d(logRegister, "Email: " + textEmail.getText());
        Log.d(logRegister, "Password: " + textPassword.getText());

        Boolean validateResponse = validateForm();
        Log.d(logRegister, "Validation result: " + validateResponse);

        if(validateResponse){
            Log.d(logRegister, "Register new user, call service: " );

            //default invalid status 4 verification
            saveObj.put("statusId", 4);
            //default user value 2
            saveObj.put("typeId", 2);
            saveObj.put("name", String.valueOf(textName.getText()));
            saveObj.put("lastName", String.valueOf(textLastName.getText()));
            //phone number not required
            saveObj.put("phoneNumber", "");
            saveObj.put("email", String.valueOf(textEmail.getText()));
            saveObj.put("password", String.valueOf(textPassword.getText()));

            requestSave.put("save", saveObj);
            apiResponse = new JSONObject(apiUtil.registerNewUser(requestSave));

            Log.d(logRegister, "Save user response " + apiResponse);

            if(apiResponse.getString("data") == "badEmail"){
                textAlert.setText(getResources().getString(R.string.registerInvalidEmailResponse));
            }else{
                Log.d(logRegister, "User created successfully, redirect to code verification ");
                Bundle param = new Bundle();
                param.putString("email", apiResponse.getJSONObject("data").getString("email"));

                findNavController(viewController).navigate(R.id.verificationCodeFragment, param);

            }

        }

    }

    public boolean validateForm(){

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(String.valueOf(textName.getText()).isEmpty()){
            textName.setError(getResources().getString(R.string.registerInvalidName));
            return false;
        }
        if(String.valueOf(textLastName.getText()).isEmpty()){
            textLastName.setError(getResources().getString(R.string.registerInvalidLastName));
            return false;
        }
        if(String.valueOf(textEmail.getText()).isEmpty() || !String.valueOf(textEmail.getText()).matches(emailPattern)){
            textEmail.setError(getResources().getString(R.string.registerInvalidEmail));
            return false;
        }
        if(String.valueOf(textPassword.getText()).isEmpty() || String.valueOf(textPassword.getText()).length() < 5){
            textPassword.setError(getResources().getString(R.string.registerInvalidPassword));
            return false;
        }
        return true;
    }

    public void eventListeners(){

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Log.d(logRegister, "Check listener " + b);
                if(b){
                    textPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else if(!b){
                    textPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(logRegister, "Button listener " + Locale.getDefault().getDisplayLanguage());
                try {
                    validateAndSave();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
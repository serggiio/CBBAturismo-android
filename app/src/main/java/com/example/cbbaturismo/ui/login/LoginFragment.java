package com.example.cbbaturismo.ui.login;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbbaturismo.R;
import com.example.cbbaturismo.commonService.apiService;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import static androidx.navigation.Navigation.findNavController;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private String logLogIn = "LOG IN FRAGMENT ";
    private apiService apiUtil = new apiService();
    private JSONObject authenticateRequest = new JSONObject();
    private JSONObject authenticateResponse;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu navMenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button loginButton = view.findViewById(R.id.login);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        navigationView = getActivity().findViewById(R.id.nav_view);
        navMenu = navigationView.getMenu();

        toolbar = this.getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");


        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                Log.d(logLogIn, "On button action");

                try {
                    authenticateRequest.put("email", String.valueOf(usernameEditText.getText()));
                    authenticateRequest.put("password", String.valueOf(passwordEditText.getText()));
                    authenticateResponse = new JSONObject(apiUtil.authenticateUser(authenticateRequest));

                    Log.d(logLogIn, "AUTHENTICATE SERVICE RESPONSE: "+ authenticateResponse);
                    String code = authenticateResponse.getString("code");
                    if(code.equals("ERROR")){
                        Log.d(logLogIn, "ERROR CASE: ");
                        if(authenticateResponse.getString("message").equals("noData")){
                            loadingProgressBar.setVisibility(View.GONE);
                            showLoginFaileds(getActivity().getString(R.string.noData));
                        }else if(authenticateResponse.getString("message").equals("verification")){
                            loadingProgressBar.setVisibility(View.GONE);

                            Bundle param = new Bundle();
                            param.putString("email", authenticateRequest.getString("email"));

                            findNavController(getView()).navigate(R.id.verificationCodeFragment, param);

                        }else{
                            loadingProgressBar.setVisibility(View.GONE);
                            showLoginFaileds(getActivity().getString(R.string.error));
                        }
                    }else if(code.equals("OK")){
                        Log.d(logLogIn, "OK CASE: ");
                        //Ok case
                        loginViewModel.login(usernameEditText.getText().toString(),
                                passwordEditText.getText().toString());
                        //set login data as preferences
                        SharedPreferences preferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor objEditor = preferences.edit();
                        objEditor.putString("userData", authenticateResponse.getJSONObject("data").toString());
                        objEditor.apply();

                            navMenu.findItem(R.id.loginFragment).setVisible(false);
                            navMenu.findItem(R.id.registerFragmentFragment).setVisible(false);
                            navMenu.findItem(R.id.frameLayout17).setVisible(false);

                            navMenu.findItem(R.id.profileFragment).setVisible(true);
                            navMenu.findItem(R.id.favoriteFragment).setVisible(true);

                        findNavController(view).navigate(R.id.ConstraintLayout);
                    }else{
                        loadingProgressBar.setVisibility(View.GONE);
                        Log.d(logLogIn, "ERROR CASE ERROR: ");
                        showLoginFaileds(getActivity().getString(R.string.error));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Failed case
                //showLoginFaileds("ERROOOOOR");

            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), getActivity().getText(R.string.welcome), Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showLoginFaileds(String errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }
}
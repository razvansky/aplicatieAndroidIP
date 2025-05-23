package com.example.aplicatieandroidip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class conectarefrag extends Fragment {

    private EditText usernameEdit, passwordEdit;
    private EditText passwordEditText;
    private CheckBox rememberMeCheck;
    private Button loginButton;
    private AuthService authService;

    public conectarefrag() {
        // Required empty public constructor
    }

    public static conectarefrag newInstance() {
        return new conectarefrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conectare, container, false);

        // Initialize UI components
        usernameEdit = view.findViewById(R.id.id_conectare);
        passwordEdit = view.findViewById(R.id.parola_conectare);
        //rememberMeCheck = view.findViewById(R.id.remember_me_checkbox); // Update ID as needed
        loginButton = view.findViewById(R.id.button_conectare);

        // Setup Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://132.220.27.51/") // Don't forget trailing slash
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authService = retrofit.create(AuthService.class);

        // Button listener
        loginButton.setOnClickListener(v -> doLogin(v));

        return view;
    }

    private void doLogin(View v) {
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        boolean rememberMe = false;

        LoginRequest request = new LoginRequest(username, password, rememberMe);

        authService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    Log.d("LOGIN", "JWT Token: " + token);

                    if (rememberMe) {
                        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
                        prefs.edit().putString("jwt", token).apply();
                    }

                    Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new conectatfrag();
                    FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.framelayout);
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.framelayout,fragment).addToBackStack(null).commit();
                } else {
                    Toast.makeText(getContext(), "Login failed! " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LOGIN", "Throwable: ", t);
            }
        });
    }
}

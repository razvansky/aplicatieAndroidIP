package com.example.aplicatieandroidip;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class LoginFragment extends Fragment {


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conectare, container, false);
        Button btn = view.findViewById(R.id.button_conectare);
        btn.setOnClickListener(this::Conectare);
        return view;
    }

        private void Conectare(View v) {

        View root = v.getRootView();

        TextView id_con = root.findViewById(R.id.id_conectare);
        String id = id_con.getText().toString();

        TextView parola = root.findViewById(R.id.parola_conectare);
        String pass = parola.getText().toString();

        if (id.equals("admin") && pass.equals("123")) {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_LoginFragment_to_principalfrag);
        }
    }
}
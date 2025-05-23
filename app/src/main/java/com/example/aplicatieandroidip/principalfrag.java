package com.example.aplicatieandroidip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link principalfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class principalfrag extends Fragment {


    public principalfrag() {
        // Required empty public constructor
    }

    public static principalfrag newInstance(String param1, String param2) {
        principalfrag fragment = new principalfrag();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void functie(View view){
        new ConnectSQLTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_principal, container, false);

        Button btn = v.findViewById(R.id.button3);
        btn.setOnClickListener(this::functie);
        return v;
    }
}
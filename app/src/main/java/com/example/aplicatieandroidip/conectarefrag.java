package com.example.aplicatieandroidip;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link conectarefrag#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class conectarefrag extends Fragment {


    public static conectarefrag newInstance() {
        conectarefrag fragment = new conectarefrag();

        return fragment;
    }

    public conectarefrag() {
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
            Fragment fragment = new principalfrag();
            FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.framelayout);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.framelayout,fragment).addToBackStack(null).commit();
        }
    }
}
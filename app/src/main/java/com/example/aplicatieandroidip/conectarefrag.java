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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static conectarefrag newInstance(String param1, String param2) {
        conectarefrag fragment = new conectarefrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public conectarefrag() {
        // Required empty public constructor
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
            Fragment fragment = new conectatfrag();
            FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.framelayout);
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.framelayout,fragment).addToBackStack(null).commit();
        }
    }
}
package com.example.aplicatieandroidip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class principalfrag extends Fragment {

    List<Pacient> pacientList = new ArrayList<Pacient>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.homeRecyclerView);
        setUpPacientList();

        RecyclerListAdapter adapter = new RecyclerListAdapter(this.getContext(), pacientList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    private void setUpPacientList(){
        String[] pacientNames = getResources().getStringArray(R.array.pacient_name_txt);
        String[] pacientIDS = getResources().getStringArray(R.array.pacient_id_txt);

        for(int i = 0; i < pacientNames.length; i++)
        {
            pacientList.add(new Pacient(pacientNames[i], pacientIDS[i], R.drawable.ic_pacienticon));
        }
    }
}
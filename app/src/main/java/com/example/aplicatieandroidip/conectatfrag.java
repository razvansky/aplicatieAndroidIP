package com.example.aplicatieandroidip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link conectatfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class conectatfrag extends Fragment {

    public conectatfrag(){}
    public static conectatfrag newInstance() {
        conectatfrag fragment = new conectatfrag();

        return fragment;
    }

    FrameLayout frameLayout;
    TabLayout tabLayout;

    View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_conectat, container, false);
        // Inflate the layout for this fragment

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        frameLayout = (FrameLayout) v.findViewById(R.id.layout_intern);
        tabLayout = (TabLayout) v.findViewById(R.id.tablayout);

        getChildFragmentManager().beginTransaction().replace(R.id.layout_intern,new principalfrag()).addToBackStack(null).commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0:
                        fragment = new principalfrag();
                        break;
                    case 1:
                        fragment = new bluetoothfrag();
                        break;
                    case 2:
                        fragment = new controalefrag();
                        break;
                }

               getChildFragmentManager().beginTransaction().replace(R.id.layout_intern,fragment).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });
    }
}
package com.example.aplicatieandroidip;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);

        Fragment fragment = new conectarefrag();

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }




}
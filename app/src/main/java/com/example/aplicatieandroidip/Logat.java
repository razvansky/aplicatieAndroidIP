package com.example.aplicatieandroidip;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.logging.Logger;


public class Logat extends AppCompatActivity {
    private Handler handler = new Handler();
    private boolean isPressed = false;
    private boolean m_auto = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logat);

        ImageButton left = findViewById(R.id.left_but);
        ImageButton right = findViewById(R.id.right_but);
        ImageButton up = findViewById(R.id.up_but);
        ImageButton down = findViewById(R.id.down_but);

        left.setOnTouchListener(createHoldListener("left"));
        right.setOnTouchListener(createHoldListener("right"));
        up.setOnTouchListener(createHoldListener("up"));
        down.setOnTouchListener(createHoldListener("down"));

    }
    private View.OnTouchListener createHoldListener(String buttonName) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isPressed = true;

                        handler.post(actionRunnable(buttonName));
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isPressed = false;
                        handler.removeCallbacksAndMessages(null); // Oprește toate Runnable-urile
                        ((TextView)findViewById(R.id.textView)).setText("none");
                        return true;
                }
                return false;
            }
        };
    }

    private Runnable actionRunnable(String buttonName) {
        return new Runnable() {
            @Override
            public void run() {
                if (isPressed) {
                    ((TextView)findViewById(R.id.textView)).setText(buttonName);
                    handler.postDelayed(this,100);
                }
            }
        };
    }

    public void Mod_Auto(View v){
        m_auto = !m_auto;
        String s = Boolean.toString(m_auto);
        Log.println(Log.INFO,"mauto",s);
    }
}
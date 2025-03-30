package com.example.aplicatieandroidip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }
    public void Conectare(View v) {
        TextView id_con = findViewById(R.id.id_conectare);
        String id = id_con.getText().toString();

        TextView parola = findViewById(R.id.parola_conectare);
        String pass = parola.getText().toString();

        if (id.equals("admin") && pass.equals("123")) {
            Intent intent = new Intent(this, Logat.class);
            startActivity(intent);
        }
    }
}
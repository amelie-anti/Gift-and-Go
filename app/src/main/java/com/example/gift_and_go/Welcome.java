package com.example.gift_and_go;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        // délai de 3 secondes avant de passer à l'activité suivante
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // lancement de l'activité principale de l'application
                Intent intent = new Intent(Welcome.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

}
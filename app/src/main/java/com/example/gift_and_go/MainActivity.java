package com.example.gift_and_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button start, create,biblio;

    private static Boolean camera = false;
    private static Boolean location = false;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int LOCATION_PERMISSION_CODE = 101;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText text;
    private Button boutonOk;

    private static String action="play";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.buttonStartGame);
        create = findViewById(R.id.buttonCreateGame);
        biblio= findViewById(R.id.buttonBibliotheque);


        biblio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent biblio = new Intent(getApplicationContext(), AlbumActivity.class);
                startActivity(biblio);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                action="play";
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                start();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                action="add";
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                start();
            }
        });

    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            if(CAMERA_PERMISSION_CODE == requestCode){
                camera=true;
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE);
            }else if(LOCATION_PERMISSION_CODE == requestCode){
                location=true;
            }
        }
    }

    public void start(){
        if(camera && location){
            Intent listIntent = new Intent(this, ListChasses.class);
            listIntent.putExtra("action",action);
            startActivity(listIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camera=true;
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE);
            }
            else {
                Toast.makeText(MainActivity.this, "Vous ne pouvez pas jouer au jeux si vous n'autorisez pas l'accès à la camera", Toast.LENGTH_SHORT) .show();
            }
        }
        else if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location=true;
                start();
            } else {
                Toast.makeText(MainActivity.this, "Vous ne pouvez pas jouer au jeux si vous n'autorisez pas l'accès à la localisation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void alertDialogCommentJouer(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View commentJouer = getLayoutInflater().inflate(R.layout.activity_comment_jouer, null);
        boutonOk = (Button) commentJouer.findViewById(R.id.idBtnOk);
        dialogBuilder.setView(commentJouer);
        dialog = dialogBuilder.create();
        dialog.show();
        Button okButton = commentJouer.findViewById(R.id.idBtnOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
package com.example.gift_and_go;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class AlbumActivity extends AppCompatActivity {
    DBBDao BDD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        BDD = new DBBDao(this);
        BDD.open();
        ArrayList<Localisation> lesLocalisations = BDD.getLocalisationsFinish();
        BDD.close();

        ArrayList<Bitmap> lesImages = new ArrayList<>();

        if(lesLocalisations.size()==0){
            TextView leTextAlbum = findViewById(R.id.albumText);
            leTextAlbum.setText("Aucune photo");
        }
        for(Localisation laLocalisation : lesLocalisations){
            String base64Image = laLocalisation.getImage(); // chaîne de caractères contenant l'image en Base64

            byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            lesImages.add(bitmap);
        }

        GridView gridView = findViewById(R.id.grid_view);
        gridView.setAdapter(new AdaptateurAlbum(this, lesImages));

    }

}
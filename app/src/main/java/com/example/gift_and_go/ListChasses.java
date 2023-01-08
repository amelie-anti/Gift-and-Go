package com.example.gift_and_go;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListChasses extends AppCompatActivity {
    DBBDao BDD;
    private static String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        BDD = new DBBDao(this);
        //On récupere la valeur passé en paramétre dans le intent précedent
        Intent intent = getIntent();
        action = intent.getStringExtra("action");

        Button add = findViewById(R.id.addChasse);
        //Le bouton n'est pas visible si on lancer une chasse
        if (action.equals("play")){
            add.setVisibility(View.INVISIBLE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListChasses.this);
                View view = getLayoutInflater().inflate(R.layout.demande_ajout, null);
                builder.setView(view);

                final EditText nameInput = view.findViewById(R.id.name_input);
                Button okButton = view.findViewById(R.id.ok_button);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BDD.open();
                        Chasse uneChasse= new Chasse();
                        uneChasse.setNom(nameInput.getText().toString());
                        Integer id = (int) BDD.ajouterChasse(uneChasse);
                        BDD.close();

                        Intent map = new Intent(view.getContext(), MapActivity.class);
                        map.putExtra("action",action);
                        map.putExtra("id",id);
                        map.putExtra("nom",uneChasse.getNom());
                        startActivity(map);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //On récupére en BDD la liste des chasses
        ListView listView = findViewById(R.id.list_view);

        BDD.open();
        ArrayList<Chasse> listChasse = BDD.getAllChasses();
        BDD.close();
        //On affiche dans la listView les données récupérées
        ArrayAdapter<Chasse> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listChasse);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Lors du'un clic sur un item de la liste View, on récupére l'item Chasse séléctionné
                Chasse chasse = (Chasse) parent.getItemAtPosition(position);
                //On ouvre l'activité Map
                Intent map = new Intent(view.getContext(), MapActivity.class);
                map.putExtra("action",action);
                map.putExtra("id",chasse.getId());
                map.putExtra("nom",chasse.getNom());
                startActivity(map);

            }
        });
    }

}
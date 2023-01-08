package com.example.gift_and_go;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DBBDao {

    private static final int VERSION_BDD = 8;
    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    public DBBDao(Context context){
        maBaseSQLite = new MaBaseSQLite(context, "chasse", null, VERSION_BDD);
    }

    public void open(){
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    //Permet d'ajouter une chasse en BDD
    public long ajouterChasse(Chasse chasse){
        ContentValues values = new ContentValues();
        values.put("nom", chasse.getNom());
        return bdd.insert("chasse", null, values);
    }



    //getChasseById permet de retourner une chasse grace à son ID
    public Chasse getChasseById(Integer id){
        Cursor c = bdd.query(
                "chasse",
                new String[] {"id", "nom"},
                "id" + " = " + id ,
                null,
                null,
                null,
                null);
        return cursorToChasse(c);
    }

    public void supprimerLocalisation(int id) {
        bdd.delete("localisation", "id = " + id, null);
    }

    private Chasse cursorToChasse(Cursor c){
        if (c.getCount() == 0)
            return null;
        c.moveToFirst();
        Chasse chasse = new Chasse();
        chasse.setId(c.getInt(0));
        chasse.setNom(c.getString(1));
        return chasse;
    }

    public ArrayList<Chasse> getAllChasses() {
        ArrayList<Chasse> chasses = new ArrayList<>();
        // Récupération de toutes les chasses de la base de données
        String selectQuery = "SELECT * FROM chasse;";
        Cursor c = bdd.rawQuery(selectQuery, null);

        // Parcours du curseur pour récupérer toutes les chasses
        if (c.moveToFirst()) {
            do {
                Chasse chasse = new Chasse();
                chasse.setId(c.getInt(0));
                chasse.setNom(c.getString(1));

                // Ajout de la chasse à la liste
                chasses.add(chasse);
            } while (c.moveToNext());
        }

        // Fermeture du curseur
        c.close();

        return chasses;
    }



    //ajouterLocalisation permet d'ajouter une Localiation en bdd
    public long ajouterLocalisation(Localisation localisation){
        ContentValues values = new ContentValues();
        values.put("latitude", localisation.getLocalisation().latitude);
        values.put("longitude", localisation.getLocalisation().longitude);
        values.put("etat", localisation.getEtat());
        values.put("image", localisation.getImage());
        values.put("idChasse", localisation.getIdChasse());
        return bdd.insert("localisation", null, values);
    }

    public long modifierLocalisation(Localisation localisation){
        ContentValues values = new ContentValues();
        values.put("etat", localisation.getEtat());
        return bdd.update("localisation", values, "id = ?", new String[]{String.valueOf(localisation.getId())});
    }

    //getLocalisationById permet de retourner une localisation grace à son id
    public Localisation getLocalisationById(Integer id){
        Cursor c = bdd.query(
                "localisation",
                new String[] {"id", "latitude", "longitude", "etat", "image", "idChasse"},
                "id" + " = " + id ,
                null,
                null,
                null,
                null);
        return cursorToLocalisation(c);
    }


    //getLocalisationsByIdChasse permet de retourner les localisations associées à une chasse avec l'id Chasse
    public ArrayList<Localisation> getLocalisationsByIdChasse(Integer idChasse) {
        ArrayList<Localisation> localisations = new ArrayList<>();
        // Récupération de toutes les chasses de la base de données
        String selectQuery = "SELECT * FROM localisation where idChasse = "+idChasse+";" ;
        Cursor c = bdd.rawQuery(selectQuery, null);

        // Parcours du curseur pour récupérer toutes les chasses
        if (c.moveToFirst()) {
            do {
                Localisation localisation = new Localisation();
                localisation.setId(c.getInt(0));
                localisation.setLocalisation(new LatLng(c.getDouble(1),c.getDouble(2) ));
                localisation.setEtat(c.getInt(3)==0?false:true);
                localisation.setImage(c.getString(4));
                localisation.setIdChasse(c.getInt(c.getInt(5)));
                // Ajout de la chasse à la liste
                localisations.add(localisation);
            } while (c.moveToNext());
        }

        // Fermeture du curseur
        c.close();

        return localisations;
    }

    public ArrayList<Localisation> getLocalisationsFinish() {
        ArrayList<Localisation> localisations = new ArrayList<>();
        // Récupération de toutes les chasses de la base de données
        String selectQuery = "SELECT * FROM localisation where etat = 1;" ;
        Cursor c = bdd.rawQuery(selectQuery, null);

        // Parcours du curseur pour récupérer toutes les chasses
        if (c.moveToFirst()) {
            do {
                Localisation localisation = new Localisation();
                localisation.setId(c.getInt(0));
                localisation.setLocalisation(new LatLng(c.getDouble(1),c.getDouble(2) ));
                localisation.setEtat(c.getInt(3)==0?false:true);
                localisation.setImage(c.getString(4));
                localisation.setIdChasse(c.getInt(c.getInt(5)));
                // Ajout de la chasse à la liste
                localisations.add(localisation);
            } while (c.moveToNext());
        }

        // Fermeture du curseur
        c.close();

        return localisations;
    }

    private Localisation cursorToLocalisation(Cursor c){
        if (c.getCount() == 0)
            return null;
        c.moveToFirst();
        Localisation localisation = new Localisation();
        localisation.setId(c.getInt(0));
        localisation.setLocalisation(new LatLng(c.getDouble(1),c.getDouble(2)));
        localisation.setEtat(c.getInt(3)==1?true:false);
        localisation.setImage(c.getString(4));
        localisation.setIdChasse(c.getInt(5));
        c.close();
        return localisation;
    }
}
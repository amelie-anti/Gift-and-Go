package com.example.gift_and_go;

import java.util.ArrayList;

public class Chasse {
    private int id;
    private String nom;
    private ArrayList <Localisation> listeLocaslisation;


    public Chasse() {
    }

    public Chasse(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Localisation> getListeLocaslisation() {
        return listeLocaslisation;
    }

    public void setListeLocaslisation(ArrayList<Localisation> listeLocaslisation) {
        this.listeLocaslisation = listeLocaslisation;
    }
}



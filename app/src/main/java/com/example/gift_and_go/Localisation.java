package com.example.gift_and_go;

import com.google.android.gms.maps.model.LatLng;

public class Localisation {
    private int id;
    private int idChasse;
    private LatLng localisation;
    private Boolean etat;
    private String image;

    public Localisation() {
    }

    public Localisation(int id, int idChasse, LatLng localisation, Boolean etat, String image) {
        this.id = id;
        this.idChasse = idChasse;
        this.localisation = localisation;
        this.etat = etat;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdChasse() {
        return idChasse;
    }

    public void setIdChasse(int idChasse) {
        this.idChasse = idChasse;
    }

    public LatLng getLocalisation() {
        return localisation;
    }

    public void setLocalisation(LatLng localisation) {
        this.localisation = localisation;
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

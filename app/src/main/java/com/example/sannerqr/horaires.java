package com.example.sannerqr;

import java.io.Serializable;

public class horaires implements Serializable {
    private String texte;
    private String heure;


    public horaires(String texte  , String heure ) {
        this.texte=  texte;
        this.heure= heure;

    }

    public String getTexte() {
        return texte;
    }

    public String getHeure() {
        return  heure;
    }

    public void setTexte(String heure_arrivée) {
        texte = texte;
    }

    public void setHeure(String heure) {
        heure = heure;
    }
}

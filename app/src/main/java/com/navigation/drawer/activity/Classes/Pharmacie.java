package com.navigation.drawer.activity.Classes;


/**
 * Created by Pro on 19/02/2017.
 */


public class Pharmacie {
    private String pharmacien ;
    private String pharmacie;
    private String adresse;
    private String secteur;
    private String tel;
    private MyGPS localisation ;

    public Pharmacie(String pharmacien, String pharmacie, String adresse, String secteur, String tel, MyGPS localisation) {
        this.pharmacien = pharmacien;
        this.pharmacie = pharmacie;
        this.adresse = adresse;
        this.secteur = secteur;
        this.tel = tel;
        this.localisation = localisation;
    }

    public Pharmacie(){
        localisation = new MyGPS();
    }

    public void setLocalisation(MyGPS l){
        localisation = l;
    }

    public MyGPS getLocalisation(){
        return  localisation;
    }

    public String getPharmacien() {
        return pharmacien;
    }
    @Override
    public String toString(){
        return pharmacien+","+pharmacie+","+adresse+","+secteur+","+tel ;
    }

    public void setPharmacien(String pharmacien) {
        this.pharmacien = pharmacien;
    }

    public String getPharmacie() {
        return pharmacie;
    }

    public void setPharmacie(String pharmacie) {
        this.pharmacie = pharmacie;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


}

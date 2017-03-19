package com.navigation.drawer.activity.Classes;

/**
 * Created by Pro on 19/02/2017.
 */

public class Medecin {
    private String name ;
    private String Adresse ;
    private String tel;
    private String speciality ;
    private MyGPS localisation ;

    public Medecin(String name, String adresse, String tel, String speciality, MyGPS localisation) {
        this.name = name;
        Adresse = adresse;
        this.tel = tel;
        this.speciality = speciality;
        this.localisation = localisation;
    }

    public Medecin() {
        localisation = new MyGPS();
    }


    public void setLocalisation(MyGPS l){
        localisation = l;
    }

    public MyGPS getLocalisation(){
        return  localisation;
    }

    public String getSpeciality(){
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String toString(){
        return name +", "+Adresse+", "+tel+", "+speciality+", "+localisation+"\n";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }




}

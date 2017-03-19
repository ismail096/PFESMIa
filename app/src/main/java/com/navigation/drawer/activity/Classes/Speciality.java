package com.navigation.drawer.activity.Classes;

import java.util.Vector;

/**
 * Created by Pro on 20/02/2017.
 */

public class Speciality {

    private Vector<Medecin> Medecins;
    private String name ;

    public Speciality(){
        Medecins = new Vector<Medecin>();
    }

    public Speciality(String name) {
        Medecins = new Vector<Medecin>();
        this.name = name;
    }

    public Vector<Medecin> getMyList() {
        return Medecins;
    }

    public void addMedecin(Medecin m){
        Medecins.add(m);
    }
    public void addMedecins(Vector<Medecin>m){
        for(int i=0;i<m.size();i++)
            if(m.get(i).getName() != null)
                Medecins.add(m.get(i));
    }


    public void setMyList(Vector<Medecin> myList) {
        myList = myList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void add(Medecin m){
        Medecins.add(m);
    }


    public Medecin get(int indexD) {
        if(indexD<Medecins.size()){
            return Medecins.get(indexD);
        }
        Medecin m = new Medecin();
        m.setName("Not Found");
        return m ;
    }
}

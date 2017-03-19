package com.navigation.drawer.activity.Classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Pro on 09/03/2017.
 */

public class Clinique {
    private String name ;
    private String categorie;
    private String adresse ;
    private String tel ;
    private HashMap<String,Vector<String>> informations ;

    public Clinique(String name, String categorie, String adresse, String tel, HashMap<String, Vector<String>> informations) {
        this.name = name;
        this.categorie = categorie;
        this.adresse = adresse;
        this.tel = tel;
        this.informations = informations;
    }

    public String getInfo(){
        String ret = "" ;
        for(Map.Entry<String, Vector<String>> entry : getInformations().entrySet()) {
            String key = entry.getKey();
            Vector<String> value = entry.getValue();
            ret+=key+"affecSeparator";
            for(int i=1;i<value.size();i++){
                if(i==1)
                    ret+=value.get(1);
                else{
                    ret+="otherInfoSeparator"+value.get(i);
                }
            }
            ret+="infoSeparator";
        }
        return  ret ;
    }
    public void setInfo(String info) {
        informations = new HashMap<String, Vector<String>>();
        String[] infos = info.split("infoSeparator");
        for (String infoOne : infos) {
            String infoName = infoOne.split("affecSeparator")[0];
            String infoValues = infoOne.split("affecSeparator")[1];

            String[] values = infoValues.split("otherInfoSeparator");
            Vector<String> val = new Vector<String>();
            for (String v : values) {
                val.add(v);
            }
            informations.put(infoName, val);
        }
    }



    public Clinique() {
        informations = new HashMap<String,Vector<String>>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public HashMap<String, Vector<String>> getInformations() {
        return informations;
    }

    public void setInformations(HashMap<String, Vector<String>> informations) {
        this.informations = informations;
    }
}

package com.navigation.drawer.activity;


import android.util.Log;

import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.MyGPS;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.Classes.Speciality;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pro on 19/02/2017.
 */

public class JSONParser {


    public static List<Clinique> JSONCliniqueParser(String content) throws JSONException {

        List<Clinique> myList = new ArrayList<Clinique>();

        JSONObject jsonObject = new JSONObject(content);
        JSONArray array = jsonObject.getJSONArray("Clinique");

        for(int i=0;i<array.length();i++){

            Clinique clinique = new Clinique();
            clinique.setName(array.getJSONObject(i).get("name").toString());
            clinique.setAdresse(array.getJSONObject(i).get("adresse").toString());
            clinique.setTel(array.getJSONObject(i).get("tel").toString());
            clinique.setCategorie(array.getJSONObject(i).get("categorie").toString());
            clinique.setInfo(array.getJSONObject(i).get("info").toString());

            myList.add(clinique);
        }

        return myList ;
    }


    public static List<Pharmacie> JSONPharmacieParser(String content) throws JSONException {

        List<Pharmacie> myList = new ArrayList<Pharmacie>();

        JSONObject jsonObject = new JSONObject(content);
        JSONArray array = jsonObject.getJSONArray("Pharmacie");

        for(int i=0;i<array.length();i++){

            Pharmacie pharmacie = new Pharmacie();
            pharmacie.setPharmacie(array.getJSONObject(i).get("pharmacie").toString());
            pharmacie.setPharmacien(array.getJSONObject(i).get("pharmacien").toString());
            pharmacie.setTel(array.getJSONObject(i).get("tel").toString());
            pharmacie.setSecteur(array.getJSONObject(i).get("secteur").toString());
            pharmacie.setAdresse(array.getJSONObject(i).get("adresse").toString());

            myList.add(pharmacie);
        }

        return myList ;
    }



    public static List<Speciality> JSONSpecialitiesPARSER(String content) throws JSONException {
        List<Speciality> myList = new ArrayList<Speciality>();

        JSONObject jsonObject = new JSONObject(content);
        JSONArray array = jsonObject.getJSONArray("Medecin");

        for(int i=0;i<array.length();i++){

            Medecin medecin = new Medecin();
            medecin.setName(array.getJSONObject(i).get("name").toString());

            if(array.getJSONObject(i).get("adresse").toString().trim().equals("DAR"))
                medecin.setAdresse("Adresse inexistante");
            else
               medecin.setAdresse(array.getJSONObject(i).get("adresse").toString());

            medecin.setTel(array.getJSONObject(i).get("tel").toString());
            medecin.setSpeciality(array.getJSONObject(i).get("speciality").toString());
            boolean drap = true;
            for (int j = 0; drap && j < myList.size(); j++) {
                if (myList.get(j).getName().equals(medecin.getSpeciality())) {
                        drap = false;
                        myList.get(j).addMedecin(medecin);
                    }
                }
                if (drap) {
                    Speciality spec = new Speciality(medecin.getSpeciality());
                    spec.addMedecin(medecin);
                    myList.add(spec);
                }

        }
        return myList;
    }

    public static MyGPS getGPS(String name){
        String URL = toURL(name);
        String content = HttpManager.getData("http://maps.google.com/maps/api/geocode/xml?address=FES%20"+URL+"%20&sensor=false");
        Log.d("haaaah ==> ","http://maps.google.com/maps/api/geocode/xml?address=FES%20"+URL+"%20&sensor=false");

        try{
            double x = 0 ;
            double y = 0 ;
            boolean isXFound = false ;
            boolean isYFound = false ;
            String currentTagName = "";
            MyGPS GPS = new MyGPS();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser  = factory.newPullParser();
            parser.setInput(new StringReader(content));
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        currentTagName = parser.getName();
                        break;
                    case XmlPullParser.END_TAG:
                        currentTagName = "";
                        break;
                    case XmlPullParser.TEXT:
                        switch (currentTagName){
                            case "lat":
                                isXFound = true ;
                                GPS.setLaltitude(Double.parseDouble(parser.getText()));
                                break;
                            case "lng":
                                isYFound = true ;
                                GPS.setLongitude(Double.parseDouble(parser.getText()));
                                break;
                            default:
                                break;
                        }
                }
                if(isXFound && isYFound){
                    System.out.println(name +" "+GPS.getLaltitude()+","+GPS.getLongitude());
                    return GPS ;
                }

                eventType = parser.next();
            }
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null ;
        }
    }
    public static String toURL(String name){
        String URL = "";
        for(int i=0;i<name.length();i++){
            if(name.charAt(i)==' ')
                URL+="%20";
            else
                URL+=""+name.charAt(i);
        }
        return URL;
    }
}

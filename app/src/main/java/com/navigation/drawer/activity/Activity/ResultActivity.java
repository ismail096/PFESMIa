package com.navigation.drawer.activity.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.navigation.drawer.activity.Activity.Profiles.CliniqueProfil;
import com.navigation.drawer.activity.Activity.Profiles.MedecinProfil;
import com.navigation.drawer.activity.Activity.Profiles.PharmacieProfil;
import com.navigation.drawer.activity.Blocs.ResultBloc;
import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.MyGPS;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.R;

import java.util.HashMap;
import java.util.Vector;

public class ResultActivity extends BaseActivity implements View.OnClickListener{
    public static String searched = null ;
    public static HashMap<ResultBloc,Object> mapList = null ;
    public static boolean pharmacie, medecin, clinique, name;
    public static Vector<ResultBloc> listResult ;
    public static SQLiteDatabase myDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Main2Activity.historique.add("result");
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_result, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);
        setListResult();

        TextView message = (TextView) findViewById(R.id.resultMsg);
        message.setText(listResult.size() +" résultat(s) disponible(s) pour <"+searched+"> : ");

        LinearLayout layout = (LinearLayout) findViewById(R.id.resultAll);
        for(int i=0;i<listResult.size();i++){
            layout.addView(listResult.get(i));
            listResult.get(i).setOnClickListener(this);
            LinearLayout.LayoutParams res = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            res.setMargins(5,2,5,2);
            listResult.get(i).setLayoutParams(res);
            listResult.get(i).setBackgroundColor(Color.parseColor("#f2e2cd"));

        }
        Button back = (Button) findViewById(R.id.backresult);
        back.setOnClickListener(this);


    }

    public void setListResult() {
        listResult = new Vector<ResultBloc>();
        mapList = new HashMap<ResultBloc, Object>();
        if (name) {
            if (pharmacie) {

                Cursor c = myDB.rawQuery("SELECT * FROM Pharmacie", null);
                c.moveToFirst();


                int indPharmacie = c.getColumnIndex("pharmacie");
                if (c != null && c.getCount() != 0) {
                    do {
                        String pharmacieName = c.getString(indPharmacie);
                        if (isEquals(pharmacieName, searched)) {
                            Pharmacie pharmacieCurr;
                            int PharmacieInd = c.getColumnIndex("pharmacie");
                            int pharmacienInd = c.getColumnIndex("pharmacien");
                            int adresseInd = c.getColumnIndex("adresse");
                            int secteurInd = c.getColumnIndex("secteur");
                            int telInd = c.getColumnIndex("tel");
                            pharmacieCurr = new Pharmacie(c.getString(pharmacienInd), c.getString(PharmacieInd), c.getString(adresseInd), c.getString(secteurInd), c.getString(telInd), new MyGPS(0, 0));

                            listResult.add(new ResultBloc(getApplicationContext(), pharmacieCurr));
                            mapList.put(listResult.get(listResult.size() - 1), pharmacieCurr);
                        }
                    } while (c.moveToNext());


                }
            }
            if (medecin) {
                Cursor c = myDB.rawQuery("SELECT * FROM Medecin", null);
                c.moveToFirst();


                int indMedecin = c.getColumnIndex("name");
                if (c != null && c.getCount() != 0) {
                    do {
                        String medecinName = c.getString(indMedecin);
                        if (isEquals(medecinName, searched)) {
                            Medecin medecinCurr;
                            int nameInd = c.getColumnIndex("name");
                            int AdresseInd = c.getColumnIndex("Adresse");
                            int telInd = c.getColumnIndex("tel");
                            medecinCurr = new Medecin(c.getString(nameInd), c.getString(AdresseInd), c.getString(telInd), c.getString(c.getColumnIndex("speciality")), new MyGPS(0, 0));
                            listResult.add(new ResultBloc(getApplicationContext(), medecinCurr));
                            mapList.put(listResult.get(listResult.size() - 1), medecinCurr);
                        }
                    } while (c.moveToNext());
                }
            }
            if (clinique) {


                Cursor cu = myDB.rawQuery("SELECT * FROM Clinique", null);
                cu.moveToFirst();


                int indClinique = cu.getColumnIndex("name");
                if (cu != null && cu.getCount() != 0) {
                    do {
                        String cliniqueName = cu.getString(indClinique);
                        if (isEquals(cliniqueName, searched)) {
                            Clinique cliniqueCurr;
                            int indCateg = cu.getColumnIndex("categorie");
                            int nameInd = cu.getColumnIndex("name");
                            int adresseInd = cu.getColumnIndex("adresse");
                            int telInd = cu.getColumnIndex("tel");
                            int infoInd = cu.getColumnIndex("info");

                            cliniqueCurr = new Clinique(cu.getString(nameInd), cu.getString(indCateg), cu.getString(adresseInd), cu.getString(telInd), null);
                            cliniqueCurr.setCategorie(cu.getString(infoInd));
                            listResult.add(new ResultBloc(getApplicationContext(), cliniqueCurr));
                            mapList.put(listResult.get(listResult.size() - 1), cliniqueCurr);
                        }
                    } while (cu.moveToNext());
                }
            }
        }
    }


    public static void setVar(String Searched,boolean Pharmacie,boolean Medecin,boolean Clinique,boolean Name){
        searched = Searched ;
        pharmacie = Pharmacie;
        medecin = Medecin;
        clinique = Clinique ;
        name = Name ;
    }

    public static boolean isEquals(String a,String b){
        String A  = a.toLowerCase();
        String B = b.toLowerCase();
        return A.contains(B) || B.contains(A);
    }

    @Override
    public void onClick(View v) {
        if (v.getClass().getSimpleName().equals("Button")) {
            Button b = (Button) v;
            if (b.getText().equals("Retour")) {
                openActivity(0);

            }
        }
        if (v.getClass().getSimpleName().equals("ResultBloc")){
            ResultBloc rb = (ResultBloc) v;
            if(mapList.get(rb).getClass().getSimpleName().equals("Medecin")) {
                Intent intent = new Intent(this, MedecinProfil.class);
                Medecin curr = (Medecin) mapList.get(rb);
                MedecinProfil.currentMedecin = curr;
                startActivity(intent);
            }
            if(mapList.get(rb).getClass().getSimpleName().equals("Pharmacie")) {
                Pharmacie curr = (Pharmacie) mapList.get(rb);
                Intent intent = new Intent(this, PharmacieProfil.class);
                PharmacieProfil.currentPharmacie = curr;
                startActivity(intent);
            }
            if(mapList.get(rb).getClass().getSimpleName().equals("Clinique")) {
                Clinique curr = (Clinique) mapList.get(rb);
                Intent intent = new Intent(this, CliniqueProfil.class);
                CliniqueProfil.currentClinique = curr;
                startActivity(intent);
            }
        }
    }
}

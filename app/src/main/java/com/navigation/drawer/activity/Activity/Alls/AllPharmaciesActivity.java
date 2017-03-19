package com.navigation.drawer.activity.Activity.Alls;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Activity.Profiles.PharmacieProfil;
import com.navigation.drawer.activity.Blocs.pharmacieBloc;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.MyGPS;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.R;

import java.util.HashMap;
import java.util.Vector;

public class AllPharmaciesActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static SQLiteDatabase myDB= null;
    HashMap<Button,Pharmacie> list = new HashMap<Button,Pharmacie>();
    ScrollView scroll  = null ;
    public static String currentSecteur = null ;
    Spinner s = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        Main2Activity.historique.add("pharmacie");
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_all_pharmacies, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
        ImageView b = (ImageView) findViewById(R.id.backP);
        b.setOnClickListener(this);
        myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);

        Cursor c = myDB.rawQuery("SELECT * FROM Pharmacie",null);
        int indQuar = c.getColumnIndex("secteur");

        Vector<String> Quartier = new Vector<String>();
        c.moveToFirst();
        if (c != null && c.getCount()!=0) {
            do {
                String quar = c.getString(indQuar);
                if(!Quartier.contains(quar)){
                    Quartier.add(quar);
                }
            }while(c.moveToNext());
        }


        int n = Quartier.size();
        String[] arraySpinner = new String[n+1];

        arraySpinner[0] = "Choisissez un quartier";
        for(int i=0;i<n;i++){
            arraySpinner[i+1] = Quartier.get(i);
        }

        s = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner) {

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                convertView = super.getDropDownView(position, convertView,
                        parent);

                convertView.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams p = convertView.getLayoutParams();
                p.height = 100; // set the height
                convertView.setLayoutParams(p);

                return convertView;
            }
        };
        adapter.setDropDownViewResource(R.layout.my_spinner_textview);
        s.setAdapter(adapter);

        if(currentSecteur != null){
            setSecteur(currentSecteur);
            selectItem(currentSecteur);
        }

        s.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getClass().getSimpleName().equals("ImageView")) {
            openActivity(0);
        }
        else {
            pharmacieBloc gb = (pharmacieBloc) v;
            PharmacieProfil.currentPharmacie = gb.pharmacie ;
            Intent intent = new Intent(this,PharmacieProfil.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0) {
            String quartier = parent.getSelectedItem().toString();

            setSecteur(quartier);
        }
        else {
            scroll = (ScrollView) findViewById(R.id.scrollPharmacies);
            scroll.setVisibility(View.INVISIBLE);
        }
    }

    private void setSecteur(String quartier) {
        GridLayout myPharmacies = (GridLayout) findViewById(R.id.PharmaciesAll);

        myPharmacies.removeAllViews();

        Cursor c = myDB.rawQuery("SELECT * FROM Pharmacie",null);
        c.moveToFirst();

        Vector<Pharmacie> pharmacie = new Vector<Pharmacie>();

        int indSec = c.getColumnIndex("secteur");
        if (c != null && c.getCount()!=0) {
            do {
                String secteur = c.getString(indSec);
                if(secteur.equals(quartier)){
                    int PharmacieInd = c.getColumnIndex("pharmacie");
                    int pharmacienInd = c.getColumnIndex("pharmacien");
                    int adresseInd = c.getColumnIndex("adresse");
                    int secteurInd = c.getColumnIndex("secteur");
                    int telInd = c.getColumnIndex("tel");

                    pharmacie.add(new Pharmacie(c.getString(pharmacienInd),c.getString(PharmacieInd), c.getString(adresseInd),c.getString(secteurInd), c.getString(telInd), new MyGPS(0,0)));
                }
            }while(c.moveToNext());
        }


        myPharmacies.setRowCount(pharmacie.size()/2+1);
        myPharmacies.setColumnCount(2);


        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int halfScreenWidth = (int)(screenWidth *0.5);
        int quarterScreenWidth = (int)(halfScreenWidth * 0.5);

        for(int i=0;i<pharmacie.size();i++){
            Pharmacie phar = pharmacie.get(i);
            GridLayout.LayoutParams param =new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = halfScreenWidth - 80;
            param.setMargins(10,10,10,10);
            param.setGravity(Gravity.CENTER);
            pharmacieBloc gb = new pharmacieBloc(getApplicationContext(),phar);
            gb.setOnClickListener(this);
            gb.setLayoutParams (param);

            myPharmacies.addView(gb);
        }

        scroll = (ScrollView) findViewById(R.id.scrollPharmacies);
        scroll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void selectItem(String name){
        for(int i=0;i<s.getAdapter().getCount();i++){
            String q =(String) s.getItemAtPosition(i);
            if(q.equals(name)){
                s.setSelection(i);
                return ;
            }
        }
    }
}

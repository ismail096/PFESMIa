package com.navigation.drawer.activity.Activity.AddProfiles;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.navigation.drawer.activity.BackgroundWorker;
import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.R;

import java.util.Vector;

public class AddPharmacie extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private String current_Info = "";

    EditText nom,adress,tel,pharmacien,quartiez;
    String  str_nom, str_quartier,str_adress,str_tel,str_pharmacien;
    Spinner spinner;
    public static SQLiteDatabase myDB= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_pharmacie, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);


        myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);

        nom = (EditText) findViewById(R.id.nomP);
        adress = (EditText) findViewById(R.id.adressepAdd);
        tel = (EditText) findViewById(R.id.telpAdd);
        pharmacien=(EditText) findViewById(R.id.pharmacienAdd);
        quartiez = (EditText) findViewById(R.id.quartOth);
        quartiez.setEnabled(true);


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
        String[] arraySpinner = new String[n+2];

        arraySpinner[0] = "Choisissez un quartier";
        for(int i=0;i<n;i++){
            arraySpinner[i+1] = Quartier.get(i);
        }
        arraySpinner[n+1] = "Autre";

        spinner =(Spinner) findViewById(R.id.spinnerQuar);

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
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }


    public void addPharmacie(View view){
        if(isNetworkAvailable()) {
            str_nom = nom.getText().toString();
            str_adress = adress.getText().toString();
            str_pharmacien = pharmacien.getText().toString();
            if (quartiez.getText().toString().isEmpty()) {
                str_quartier = spinner.getSelectedItem().toString();
            } else {
                str_quartier = quartiez.getText().toString();
            }

            str_tel = tel.getText().toString();

            if (str_nom.isEmpty() || str_adress.isEmpty() || str_tel.isEmpty() || str_pharmacien.isEmpty() || str_quartier.isEmpty())
                Toast.makeText(getApplicationContext(), "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
            else {
                String type = "Register";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute("Pharmacie", type, str_nom, str_quartier, str_adress, str_tel, str_pharmacien);
                openActivity(0);
            }
        }
        else
            Toast.makeText(getApplicationContext(),"Connectez vous a internet et réessayez svp",Toast.LENGTH_LONG).show();
    }
    private  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).equals("Autre")){
            quartiez.setEnabled(true);
        }
        else
            quartiez.setEnabled(false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

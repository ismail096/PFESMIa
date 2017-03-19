package com.navigation.drawer.activity.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.navigation.drawer.activity.Activity.Alls.AllCliniquesActivity;
import com.navigation.drawer.activity.Activity.Alls.AllMedecinsActivity;
import com.navigation.drawer.activity.Activity.Alls.AllPharmaciesActivity;
import com.navigation.drawer.activity.Activity.Profiles.CliniqueProfil;
import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.Classes.ListGarde;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.MyGPS;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.Classes.Speciality;
import com.navigation.drawer.activity.HttpManager;
import com.navigation.drawer.activity.JSONParser;
import com.navigation.drawer.activity.R;

import java.util.List;


public class SearchActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    CheckBox cPha = null, cCli = null, cMed = null;
    EditText searchField = null;
    Button search = null;
    ImageView addMedecin = null, addClinique = null, addPharmacie = null;
    public SQLiteDatabase myDB = null;
    double userlocatLa;
    double userlocatLn;
    public boolean loginVerifed ;
    ProgressDialog pDialog;

    public boolean firstTime = true ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Main2Activity.historique.add("search");

        super.onCreate(savedInstanceState);

        myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);


        getLayoutInflater().inflate(R.layout.activity_search, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);


        searchField = (EditText) findViewById(R.id.editText3);

        cPha = (CheckBox) findViewById(R.id.CheckPharmacies);
        cMed = (CheckBox) findViewById(R.id.CheckMedecins);
        cCli = (CheckBox) findViewById(R.id.CheckCliniques);

        search = (Button) findViewById(R.id.Search);
        search.setOnClickListener(this);

        ImageView Closer = (ImageView) findViewById(R.id.proche);
        Closer.setOnClickListener(this);

        addMedecin = (ImageView) findViewById(R.id.getMedecins);
        addMedecin.setOnClickListener(this);
        addClinique = (ImageView) findViewById(R.id.getCliniques);
        addClinique.setOnClickListener(this);
        addPharmacie = (ImageView) findViewById(R.id.getPharmacies);
        addPharmacie.setOnClickListener(this);

        new GetGPS(SearchActivity.this);
    }



    private void DoTask() throws InterruptedException {

        if (isNetworkAvailable()) {

            if (ListGarde.pharmacieList != null) {
                double dis = 1000000000;
                Pharmacie closer = null;
                Location currentLocation = new Location("");
                currentLocation.setLatitude(GetGPS.location.getLatitude());
                currentLocation.setLongitude(GetGPS.location.getLongitude());
                Location closerLoc   = null ;
                for (int i = 0; i < ListGarde.pharmacieList.size(); i++) {

                    Pharmacie currentPharmacie = ListGarde.pharmacieList.get(i);
                    Location crLoc = new Location("");

                    crLoc.setLongitude(currentPharmacie.getLocalisation().getLongitude());
                    crLoc.setLatitude(currentPharmacie.getLocalisation().getLaltitude());

                    double currDistance = currentLocation.distanceTo(crLoc);


                    if (dis > currDistance) {
                        dis = currDistance;
                        closerLoc = crLoc ;
                        closer = currentPharmacie;
                    }
                }

                MapActivity.currentLocation = currentLocation ;
                MapActivity.searchedLocation = closerLoc;
                MapActivity.currentObject = closer;

                startActivity(new Intent(this, MapActivity.class));
            }
        } else {
            Toast.makeText(getApplicationContext(), "This service is not available in Offline", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position - 1) {
            case 0:
                openActivity(2);
                break;
            case 1:
                openActivity(1);
                break;
            case 2:
                openActivity(3);
                break;
            case 3:
                openActivity(4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getClass().getSimpleName().equals("Button")) {
            Button b = (Button) v;
            if (b.getText().equals("Search")) {
                if (searchField.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Entrer quelque chose svp", Toast.LENGTH_SHORT).show();
                else {
                    ResultActivity.setVar(searchField.getText().toString(), cPha.isChecked(), cMed.isChecked(), cCli.isChecked(),true);
                    Intent intent = new Intent(this, ResultActivity.class);
                    startActivity(intent);
                }
            }
        }
        if (v.getClass().getSimpleName().equals("ImageView")) {
            ImageView b = (ImageView) v;
            if (b.getId() == R.id.proche) {
                new MyTask().execute() ;
            }
            if (b.getId() == R.id.getCliniques) {
                AllCliniquesActivity.currentCateg = null ;
                openActivity(4);
            }
            if(b.getId() == R.id.getMedecins){
                AllMedecinsActivity.currentSpeciality = null ;
                openActivity(3);
            }
            if(b.getId() == R.id.getPharmacies){
                AllPharmaciesActivity.currentSecteur = null ;
                openActivityFrom(2);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public class MyTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Please Wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            if (firstTime && isNetworkAvailable()) {
                firstTime = false;
                try {
                    if (ListGarde.pharmacieList != null) {
                        for (int i = 0; i < ListGarde.pharmacieList.size(); i++) {
                            Pharmacie currentPharmacie = ListGarde.pharmacieList.get(i);
                            boolean drap = false;

                            MyGPS t = JSONParser.getGPS(currentPharmacie.getPharmacie() + " PHARMACIE");
                            if (t != null) {
                                currentPharmacie.setLocalisation(t);
                                drap = true;
                            }
                            if (!drap) {
                                t = JSONParser.getGPS(currentPharmacie.getAdresse() + " PHARMACIE");
                                if (t != null) {
                                    currentPharmacie.setLocalisation(t);
                                    drap = true;
                                }
                            }
                            if (!drap) {
                                t = JSONParser.getGPS(currentPharmacie.getSecteur() + " PHARMACIE");
                                if (t != null) {
                                    currentPharmacie.setLocalisation(t);
                                }
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
            loginVerifed = true;
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (loginVerifed) {
                pDialog.dismiss();
                if(isNetworkAvailable()) {
                    try {
                        if(GetGPS.location!=null) {
                            MapActivity.currentLocation = GetGPS.location;
                            DoTask();
                        }
                        else{
                            new GetGPS(SearchActivity.this);
                            Toast.makeText(SearchActivity.this, "Activez gps et attendez svp ...", Toast.LENGTH_SHORT).show();
                            openActivity(0);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Connectez vous a l'internet et redemarrez l'application", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }
}


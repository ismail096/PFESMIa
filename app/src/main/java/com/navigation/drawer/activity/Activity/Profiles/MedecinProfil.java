package com.navigation.drawer.activity.Activity.Profiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.navigation.drawer.activity.Activity.Alls.AllCliniquesActivity;
import com.navigation.drawer.activity.Activity.Alls.AllMedecinsActivity;
import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.FavorisActivity;
import com.navigation.drawer.activity.Activity.GetGPS;
import com.navigation.drawer.activity.Activity.SearchActivity;
import com.navigation.drawer.activity.Activity.UploadData;
import com.navigation.drawer.activity.JSONParser;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Activity.MapActivity;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.MyGPS;
import com.navigation.drawer.activity.R;

public class MedecinProfil extends BaseActivity implements View.OnClickListener {

    public boolean loginVerifed = false;
    public String currentTel = null;
    public static MyGPS result = null;
    public static boolean isFavoris = false;
    public static Medecin currentMedecin = null;
    ImageView fav = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_medecin_profil, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);


        TextView speciality = (TextView) findViewById(R.id.SpecialityM);
        ImageView tel = (ImageView) findViewById(R.id.telM);
        TextView name = (TextView) findViewById(R.id.MedecinName);
        TextView Adresse = (TextView) findViewById(R.id.AdresseM);

        ImageView back = (ImageView) findViewById(R.id.retourmp);
        back.setOnClickListener(this);

        name.setText("Dr. " + currentMedecin.getName());
        speciality.setText(currentMedecin.getSpeciality());
        TextView teleph = (TextView) findViewById(R.id.telMM);
        currentTel = currentMedecin.getTel();
        teleph.setHint(currentTel);
        tel.setOnClickListener(this);


        String mainString = currentMedecin.getAdresse();
        String[] stringArray = mainString.split("\\s+");
        String tmpString = "";
        StringBuffer  finalString = new StringBuffer();
        for (String singleWord : stringArray) {
            if ((tmpString + singleWord + " ").length() > 28) {
                finalString.append(tmpString + "\n");

                tmpString = singleWord + " ";
            } else {
                tmpString = tmpString + singleWord + " ";
            }
        }

        if (tmpString.length() > 0) {
            finalString.append(tmpString);
        }
        Adresse.setText(finalString);

        fav = (ImageView) findViewById(R.id.fav_med);
        if (FavorisActivity.lf.contains(currentMedecin)) {
            isFavoris = true;
            fav.setImageDrawable(getResources().getDrawable(R.drawable.fav));
        } else {
            isFavoris = false;
            fav.setImageDrawable(getResources().getDrawable(R.drawable.notfav));
        }
        fav.setOnClickListener(this);


        ImageView localisation = (ImageView) findViewById(R.id.localMedecin);
        localisation.setOnClickListener(this);

        new GetGPS(MedecinProfil.this);

    }

    @Override
    public void onClick(View v) {
         if (v.getClass().getSimpleName().equals("ImageView")) {
            ImageView curr = (ImageView) v;

            if (curr.getId() == R.id.telM || curr.getId() == R.id.retourmp || curr.getId() == R.id.localMedecin) {
                if(curr.getId() == R.id.telM) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + currentTel));
                        startActivity(callIntent);
                    } catch (SecurityException err) {
                        System.out.println(err);
                    }
                }
                if (curr.getId() == R.id.retourmp) {
                    int search = Main2Activity.historique.lastIndexOf("search");
                    int favoris = Main2Activity.historique.lastIndexOf("favoris");
                    int medecins = Main2Activity.historique.lastIndexOf("medecin");

                    int m = Math.max(search,Math.max(favoris,medecins));

                    if(m==search)
                        openActivity(0);
                    if(m==medecins) {
                        AllMedecinsActivity.currentSpeciality = currentMedecin.getSpeciality();
                        openActivityFrom(3);
                    }
                    if(m==favoris)
                        openActivity(5);
                }
                if (curr.getId() == R.id.localMedecin) {
                    if(isNetworkAvailable())
                        new MyTask().execute();
                    else
                        Toast.makeText(getApplicationContext(),"connectez vous a internet et activez gps et reessayer",Toast.LENGTH_LONG).show();

                }
            }
            else{
                if(isFavoris){
                    isFavoris = false ;
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.notfav));
                    Main2Activity.removeFav("Medecin",currentMedecin.getName());
                }
                else{
                    isFavoris = true ;
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.fav));
                    Main2Activity.insertFav("Medecin",currentMedecin.getName());
                }
            }
         }
    }
    public class MyTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MedecinProfil.this);
            pDialog.setMessage("Please Wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if(currentMedecin.getLocalisation().getLongitude()!=0){

                Location loc = new Location("");
                loc.setLatitude(currentMedecin.getLocalisation().getLaltitude());
                loc.setLongitude(currentMedecin.getLocalisation().getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentMedecin;


                loginVerifed = true;
                return null ;
            }

            MyGPS t = JSONParser.getGPS(currentMedecin.getName());
            if(t != null){
                currentMedecin.setLocalisation(t);

                Location loc = new Location("");
                loc.setLatitude(currentMedecin.getLocalisation().getLaltitude());
                loc.setLongitude(currentMedecin.getLocalisation().getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentMedecin;

                loginVerifed = true;

                return null ;
            }
            t = JSONParser.getGPS(currentMedecin.getName()+" "+currentMedecin.getAdresse());
            if(t != null){
                currentMedecin.setLocalisation(t);

                Location loc = new Location("");
                loc.setLatitude(currentMedecin.getLocalisation().getLaltitude());
                loc.setLongitude(currentMedecin.getLocalisation().getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentMedecin;


                loginVerifed = true;
                return null ;
            }
            t = JSONParser.getGPS(currentMedecin.getAdresse() );
            if(t != null){
                currentMedecin.setLocalisation(t);
                Location loc = new Location("");
                loc.setLatitude(currentMedecin.getLocalisation().getLaltitude());
                loc.setLongitude(currentMedecin.getLocalisation().getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentMedecin;

                loginVerifed = true;
                return null ;
            }
            loginVerifed = false;
            return  null ;
        }

        @Override
        protected void onPostExecute(String s) {
            if(loginVerifed == true)
            {
                pDialog.dismiss();
                if(GetGPS.location!=null) {
                    MapActivity.currentLocation = GetGPS.location;
                    startActivity(new Intent(MedecinProfil.this, MapActivity.class));
                }
                else{
                    new GetGPS(MedecinProfil.this);
                    Toast.makeText(MedecinProfil.this, "Activez gps et attendez svp ...", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
            else {
                pDialog.dismiss();
                Toast.makeText(MedecinProfil.this, "not available", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

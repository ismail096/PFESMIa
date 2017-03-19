package com.navigation.drawer.activity.Activity.Profiles;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.navigation.drawer.activity.Activity.Alls.AllCliniquesActivity;
import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.FavorisActivity;
import com.navigation.drawer.activity.Activity.GetGPS;
import com.navigation.drawer.activity.Activity.SearchActivity;
import com.navigation.drawer.activity.JSONParser;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Activity.MapActivity;
import com.navigation.drawer.activity.Blocs.InfoBloc;
import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.Classes.MyGPS;
import com.navigation.drawer.activity.R;

import java.util.Map;
import java.util.Vector;

public class CliniqueProfil extends BaseActivity implements View.OnClickListener {

    public static Clinique currentClinique = null;
    public String currentTel = null;
    ProgressDialog pDialog;
    public boolean loginVerifed = false;
    public static boolean isFavoris = false;
    LinearLayout ll =  null ;

    /*public boolean loginVerifed = false;
    public static MyGPS result = null;
    public static Speciality currentSpeciality = null;*/
    ImageView fav = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_clinique_profil, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        TextView telC = (TextView) findViewById(R.id.telCM);
        TextView categorie = (TextView) findViewById(R.id.CategorieC);
        ImageView tel = (ImageView) findViewById(R.id.telC);
        TextView name = (TextView) findViewById(R.id.CliniqueName);
        TextView Adresse = (TextView) findViewById(R.id.AdresseC);

        ImageView back = (ImageView) findViewById(R.id.retourcp);
        back.setOnClickListener(this);

        name.setText(currentClinique.getName());
        categorie.setText(currentClinique.getCategorie());

        currentTel = currentClinique.getTel();

        tel.setOnClickListener(this);

        String mainString = currentClinique.getAdresse();
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


        telC.setText(currentTel);

        fav = (ImageView) findViewById(R.id.fav_cli);
        if (FavorisActivity.lf.contains(currentClinique)) {
            isFavoris = true;
            fav.setImageDrawable(getResources().getDrawable(R.drawable.fav));
        } else {
            isFavoris = false;
            fav.setImageDrawable(getResources().getDrawable(R.drawable.notfav));
        }

        fav.setOnClickListener(this);

        ll = (LinearLayout) findViewById(R.id.cliniqueInformations);

        for(Map.Entry<String, Vector<String>> entry : currentClinique.getInformations().entrySet()) {
            String key = entry.getKey();
            Vector<String> value = entry.getValue();
            InfoBloc il = new InfoBloc(getApplicationContext(),key,value);

            ll.addView(il);

        }
        ImageView localisation = (ImageView) findViewById(R.id.localClinique);
        localisation.setOnClickListener(this);

        new GetGPS(CliniqueProfil.this);

    }


    @Override
    public void onClick(View v) {
        if (v.getClass().getSimpleName().equals("ImageView")) {
            ImageView curr = (ImageView) v;

            if (curr.getId() == R.id.telC || curr.getId() == R.id.retourcp || curr.getId() == R.id.localClinique) {
                if(curr.getId() == R.id.telC) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + currentTel));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
                if (curr.getId() == R.id.retourcp) {

                    int cliniques = Main2Activity.historique.lastIndexOf("clinique");
                    int search = Main2Activity.historique.lastIndexOf("search");
                    int favoris = Main2Activity.historique.lastIndexOf("favoris");


                    int m = Math.max(search,Math.max(favoris,cliniques));

                    if(m==search)
                        openActivity(0);
                    if(m==cliniques)
                        AllCliniquesActivity.currentCateg = currentClinique.getCategorie() ;
                        openActivityFrom(4);
                    if(m==favoris)
                        openActivity(5);

                }
                if (curr.getId() == R.id.localClinique) {
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
                    Main2Activity.removeFav("Clinique",currentClinique.getName());
                }
                else{
                    isFavoris = true ;
                    fav.setImageDrawable(getResources().getDrawable(R.drawable.fav));
                    Main2Activity.insertFav("Clinique",currentClinique.getName());
                }
            }
        }
    }
    public class MyTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CliniqueProfil.this);
            pDialog.setMessage("Please Wait");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            MyGPS t = JSONParser.getGPS(currentClinique.getName());
            if(t != null){

                Location loc = new Location("");
                loc.setLatitude(t.getLaltitude());
                loc.setLongitude(t.getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentClinique;

                loginVerifed = true;

                return null ;
            }
            t = JSONParser.getGPS(currentClinique.getName()+" "+currentClinique.getAdresse());
            if(t != null){

                Location loc = new Location("");
                loc.setLatitude(t.getLaltitude());
                loc.setLongitude(t.getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentClinique;


                loginVerifed = true;
                return null ;
            }
            t = JSONParser.getGPS(currentClinique.getAdresse() );
            if(t != null){
                Location loc = new Location("");
                loc.setLatitude(t.getLaltitude());
                loc.setLongitude(t.getLongitude());

                MapActivity.searchedLocation = loc;
                MapActivity.currentObject = currentClinique;

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
                    startActivity(new Intent(CliniqueProfil.this, MapActivity.class));
                }
                else{
                    new GetGPS(CliniqueProfil.this);
                    Toast.makeText(CliniqueProfil.this, "Activez gps et attendez svp ...", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
            else{
                pDialog.dismiss();
                Toast.makeText(CliniqueProfil.this, "not available", Toast.LENGTH_SHORT).show();
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
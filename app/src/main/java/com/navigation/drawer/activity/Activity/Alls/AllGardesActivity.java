package com.navigation.drawer.activity.Activity.Alls;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Activity.Profiles.PharmacieProfil;
import com.navigation.drawer.activity.Blocs.gardeBloc;
import com.navigation.drawer.activity.Classes.ListGarde;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.R;

import java.util.HashMap;

public class AllGardesActivity extends BaseActivity implements View.OnClickListener{
    boolean FirstTime ;
    public  static boolean isUploaded = false ;
    boolean isDone = false ;
    GridLayout myPharmacies = null ;
    public static SQLiteDatabase myDB= null;

    HashMap<Button,Pharmacie> list = new HashMap<Button,Pharmacie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Main2Activity.historique.add("garde");
        FirstTime = true ;
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_all_gardes, frameLayout);

        myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);


        ImageView b = (ImageView) findViewById(R.id.backPG);
        b.setOnClickListener(this);

        if(!isUploaded) {
            if (isNetworkAvailable()) {
                DoTask();
            } else {
            }
            isUploaded = true ;
        }
        else{
            DoTask();
        }


    }

    private void DoTask() {

        if(ListGarde.pharmacieList != null) {

            myPharmacies = (GridLayout) findViewById(R.id.PharmaciesGarde);
            myPharmacies.setRowCount(ListGarde.pharmacieList.size()+1);
            if(FirstTime)
                FirstTime = false ;
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            int halfScreenWidth = (int)(screenWidth *0.5);
            int quarterScreenWidth = (int)(halfScreenWidth * 0.5);

            for(int i=0;i<ListGarde.pharmacieList.size();i++){
                Pharmacie pharmacie = ListGarde.pharmacieList.get(i);
                GridLayout.LayoutParams param =new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = halfScreenWidth - 80;
                param.setMargins(10,10,10,10);
                param.setGravity(Gravity.CENTER);
                gardeBloc gb = new gardeBloc(getApplicationContext(),pharmacie);
                gb.setOnClickListener(this);
                gb.setLayoutParams (param);

                myPharmacies.addView(gb);
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"Connectez vous a internet et redemarrez l'application svp",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getClass().getSimpleName().equals("ImageView")) {
            openActivity(0);
        }
        else {
            gardeBloc gb = (gardeBloc) v;
            PharmacieProfil.currentPharmacie = gb.pharmacie ;
            Intent intent = new Intent(this,PharmacieProfil.class);
            startActivity(intent);
            }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

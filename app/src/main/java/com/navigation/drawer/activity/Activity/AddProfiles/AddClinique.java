package com.navigation.drawer.activity.Activity.AddProfiles;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.navigation.drawer.activity.BackgroundWorker;
import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.Blocs.LayoutEdit;
import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.R;

import java.util.HashMap;
import java.util.Vector;

public class AddClinique  extends BaseActivity implements AdapterView.OnItemSelectedListener {


    private String current_Info = "";

    EditText name = null , adresse = null , tel = null ;
    RadioButton administ = null ;
    String current_Categorie = null ;
    LinearLayout myLayout = null ;
    TextView tv = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_clinique, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        myLayout = (LinearLayout) findViewById(R.id.clInfo);


        tv = new TextView(getApplicationContext());
        tv.setText("Ajouter une information");
        tv.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        layoutParams.weight = 1 ;
        myLayout.addView(tv);
        tv.setLayoutParams(layoutParams);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myLayout.removeView(tv);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                myLayout.addView(new LayoutEdit(getApplicationContext(),lp));
                myLayout.addView(tv);
                tv.setOnClickListener(this);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.categ_choice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Categorie_choice, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        name = (EditText) findViewById(R.id.clName);
        adresse = (EditText) findViewById(R.id.clAdresse);
        tel = (EditText) findViewById(R.id.clTel);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=0)
            current_Categorie = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void AddClinique(View view) {
        if(isNetworkAvailable()) {
            Clinique cl = new Clinique();

            if (name.getText().toString().isEmpty() || adresse.getText().toString().isEmpty() || current_Categorie == null || tel.getText().toString().isEmpty())
                Toast.makeText(getApplicationContext(), "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
            else {
                cl.setName(name.getText().toString());
                cl.setAdresse(adresse.getText().toString());
                cl.setTel(tel.getText().toString());
                cl.setCategorie(current_Categorie);
                HashMap<String, Vector<String>> map = new HashMap<String, Vector<String>>();
                for (int i = 0; i < myLayout.getChildCount(); i++) {
                    if (myLayout.getChildAt(i).getClass().getSimpleName().equals("LayoutEdit")) {
                        LayoutEdit le = (LayoutEdit) myLayout.getChildAt(i);
                        map.put(le.getInfoName().getText().toString(), le.getInformations());
                    }
                }
                cl.setInformations(map);
                BackgroundWorker.currentClinique = cl;
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute("Clinique", "Register");
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
}

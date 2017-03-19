package com.navigation.drawer.activity.Activity.AddProfiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.R;

public class AddProfil extends BaseActivity implements View.OnClickListener {

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_profil, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        }

    @Override
    public void onClick(View v) {

    }

    public void cliniqueCLicked(View view) {
        startActivity(new Intent(this,AddClinique.class));
    }

    public void medecinCLicked(View view) {
        startActivity(new Intent(this,AddMedecin.class));
    }


    public void pharmacieCLicked(View view) {
        startActivity(new Intent(this,AddPharmacie.class));
    }

    public void back(View view) {
        openActivity(0);
    }
}

package com.navigation.drawer.activity.Blocs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.navigation.drawer.activity.Classes.Clinique;
import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.R;

/**
 * Created by Pro on 25/02/2017.
 */

public class ResultBloc extends LinearLayout {

    LayoutParams layoutParams = null ;

    public ResultBloc(Context context,Object object) {
        super(context);

        setOrientation(VERTICAL);

        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        if(object.getClass().getSimpleName().equals("Medecin")){
            Log.d("haaa Tbeb","    ");
            Medecin medecin = (Medecin) object ;
            addView(getNameTV(medecin.getName()));
            addView(getObjectTV("Medecin - "+medecin.getSpeciality()));
        }
        if(object.getClass().getSimpleName().equals("Pharmacie")){
            Log.d("haaa Pharmacie","    ");
            Pharmacie pharmacie = (Pharmacie) object ;
            addView(getNameTV(pharmacie.getPharmacie()));
            addView(getObjectTV("Pharmacie"));
        }
        if(object.getClass().getSimpleName().equals("Clinique")){
            Clinique clinique = (Clinique) object ;
            addView(getNameTV(clinique.getName()));
            addView(getObjectTV("Clinique"));
        }

        setBackground(getResources().getDrawable(R.drawable.edit_text));
    }
    public TextView getNameTV(String name){
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(layoutParams);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setText(name);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(16);

        return tv ;
    }
    public TextView getObjectTV(String name){
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(layoutParams);
        tv.setText(name);
        tv.setTextColor(Color.parseColor("#999999"));
        tv.setTextSize(10);
        return tv ;
    }
}

package com.navigation.drawer.activity.Blocs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.navigation.drawer.activity.R;

import java.util.Vector;

/**
 * Created by Pro on 08/03/2017.
 */

public class InfoLayout extends LinearLayout {
    Context context = null ;
    LinearLayout myLayout = null ;
    TextView tv = null ;
    LinearLayout.LayoutParams layoutparams = null ;

    public InfoLayout(Context context) {
        super(context);

        setOrientation(VERTICAL);

        this.context = context;
        myLayout = this ;


            layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        addView(getEditText());

        tv = new TextView(context);
        tv.setText("Ajouter une information Info");
        tv.setGravity(TEXT_ALIGNMENT_CENTER);
        addView(tv);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                myLayout.removeView(tv);
                 myLayout.addView(getEditText());
                myLayout.addView(tv);
                tv.setOnClickListener(this);
            }
        });

    }
    EditText getEditText(){
        EditText et = new EditText(context);
        et.setTextColor(Color.BLACK);
        et.setLayoutParams(layoutparams);
        et.setHint("information detail");
        et.getBackground().setColorFilter(getResources().getColor(R.color.cardview_dark_background), PorterDuff.Mode.SRC_ATOP);
        return  et ;
    }
    Vector<String> getInfos(){
        Vector<String> myList = new Vector<String>();
        for(int i=0;i<this.getChildCount();i++){
            if(getChildAt(i).getClass().getSimpleName().equals("EditText")){
                EditText ed = (EditText) getChildAt(i);
                myList.add(ed.getText().toString());
            }
        }
        return myList;
    }
}

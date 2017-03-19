package com.navigation.drawer.activity.Blocs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.navigation.drawer.activity.R;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Pro on 08/03/2017.
 */

public class LayoutEdit extends LinearLayout {

    EditText infoName = null ;
    InfoLayout myInfo = null ;

    public EditText getInfoName() {
        return infoName;
    }

    public void setInfoName(EditText infoName) {
        this.infoName = infoName;
    }

    public InfoLayout getMyInfo() {
        return myInfo;
    }

    public void setMyInfo(InfoLayout myInfo) {
        this.myInfo = myInfo;
    }

    public LayoutEdit(Context context, LayoutParams lp) {
        super(context);

        setBackground(getResources().getDrawable(R.drawable.edit_text));
        setPadding(10,10,10,10);

        setWeightSum(1);

        setLayoutParams(lp);

        setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoute = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);

        layoute.weight = (float) 0.33;


        infoName = new EditText(context);
        infoName.setTextColor(Color.BLACK);
        infoName.getBackground().setColorFilter(getResources().getColor(R.color.cardview_dark_background), PorterDuff.Mode.SRC_ATOP);

        addView(infoName);

        infoName.setLayoutParams(layoute);


        LinearLayout.LayoutParams layoutL = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutL.weight = (float) 0.66;

        myInfo = new InfoLayout(context);
        addView(myInfo);
        myInfo.setLayoutParams(layoutL);

    }
    public Vector<String> getInformations(){
        Vector<String> myList = new Vector<String>();
        myList.add(infoName.getText().toString());
        myList.addAll(myInfo.getInfos());
        return  myList ;
    }
}

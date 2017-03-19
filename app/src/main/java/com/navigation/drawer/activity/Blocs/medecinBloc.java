package com.navigation.drawer.activity.Blocs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.navigation.drawer.activity.Classes.Medecin;
import com.navigation.drawer.activity.Classes.Pharmacie;
import com.navigation.drawer.activity.R;

/**
 * Created by Pro on 14/03/2017.
 */

public class medecinBloc extends LinearLayout {
    public Medecin medecin = null ;
    public medecinBloc(Context context , Medecin medecin) {
        super(context);
        setBackground(getResources().getDrawable(R.drawable.edit_text));
        this.medecin = medecin;
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setBackgroundColor(Color.parseColor("#20B2AA"));
        setPadding(10,10,10,10);

        ImageView img = new ImageView(context);
        img.setImageDrawable(getResources().getDrawable(R.drawable.logomedecin));
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        addView(img);
        img.setLayoutParams(lp);

        TextView tv = new TextView(context);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(Color.parseColor("#FFFFF0"));
        double scale = context.getResources().getDisplayMetrics().density;
        int height = (int)(50 * scale + 0.5f);
        LayoutParams lpt = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        lpt.gravity = Gravity.CENTER;
        tv.setText(medecin.getName());
        tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        addView(tv);
        tv.setLayoutParams(lpt);
    }

}

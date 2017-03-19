package com.navigation.drawer.activity.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pro on 19/02/2017.
 */

public class ListGarde {

    public static List<Pharmacie> pharmacieList ;

    ListGarde(){
        pharmacieList = new ArrayList<Pharmacie>();
    }

    public Pharmacie get(int i){
        if(pharmacieList.size()>i)
            return pharmacieList.get(i);
        return null;
    }
}
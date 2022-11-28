package com.dfs.dfslineupbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// Source: https://stackoverflow.com/questions/12744337/how-to-keep-android-applications-always-be-logged-in-state

public class UserLocation {

    static final String USER_LOC = "userLocation";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static String getUserLocation(Context context){
        return getSharedPreferences(context).getString(USER_LOC,"");
    }

    public static void setUserLocation(Context context, String location){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_LOC, location);
        editor.commit();
    }


    //TODO: MAKE SURE LOCATION ISN'T WIPED WHEN USERS LOGIN/OUT
    public static void clearUserLocation(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

}

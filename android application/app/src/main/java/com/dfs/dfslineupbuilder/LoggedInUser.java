package com.dfs.dfslineupbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// Source: https://stackoverflow.com/questions/12744337/how-to-keep-android-applications-always-be-logged-in-state

public class LoggedInUser {

    static final String USER_ID = "userId";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static String getLoggedInUser(Context context){
        return getSharedPreferences(context).getString(USER_ID,"");
    }

    public static void setLoggedInUser(Context context, String userId){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public static void clearLoggedInUser(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

}

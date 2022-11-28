package com.dfs.dfslineupbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// Source: https://stackoverflow.com/questions/12744337/how-to-keep-android-applications-always-be-logged-in-state

public class LoggedInUser {

    static final String USER_ID = "userId";
    static final String USER_NAME = "userName";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static String getLoggedInUser(Context context){
        return getSharedPreferences(context).getString(USER_ID,"");
    }

    public static String getLoggedInUserName(Context context){
        return getSharedPreferences(context).getString(USER_NAME,"");
    }

    public static void setLoggedInUser(Context context, String userId, String userName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER_ID, userId);
        String editedName = userName.substring(0,userName.indexOf("@"));
        editor.putString(USER_NAME, editedName);
        editor.commit();
    }

    public static void clearLoggedInUser(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(USER_ID);
        editor.remove(USER_NAME);
        editor.commit();
    }

}

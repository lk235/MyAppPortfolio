package com.example.android.myappportfolio.topMovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.android.myappportfolio.R;
import com.example.android.myappportfolio.topMovies.sync.MovieSyncAdapter;

/**
 * Created by lk235 on 2017/5/8.
 */

public class Utility {

    private static final String strSeparator = ",";
    public static String getPrefSortSetting(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        });
        String cateGroySetting = sharedPreferences.getString(context.getString(R.string.pref_sort_key),
                context.getString((R.string.pref_sort_default)));

        return cateGroySetting;
    }

    public static int getPrefSyncSetting(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        });
        String syncSetting = sharedPreferences.getString(context.getString(R.string.pref_sync_key),
                context.getString((R.string.pref_sync_default)));

        return Integer.parseInt(syncSetting);
    }



    public static String convertArrayToString(String[] array){


        String str = "";
        for (int i = 0; i<array.length; i++) {
            str = str+array[i];
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    @SuppressWarnings("ResourceType")
    static public @MovieSyncAdapter.ServerStatus
    int getServerStatus(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(context.getString(R.string.pref_server_status_key),
                MovieSyncAdapter.SERVER_STATUS_UNKNOWN);

    }
}

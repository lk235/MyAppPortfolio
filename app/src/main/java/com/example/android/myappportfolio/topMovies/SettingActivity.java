package com.example.android.myappportfolio.topMovies;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.android.myappportfolio.R;

/**
 * Created by lk235 on 2017/3/15.
 */

public class SettingActivity extends AppCompatActivity {

    public static final String KEY_PREF_SORT_TYPE = "Sort Type";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_toolbar);
        getFragmentManager().beginTransaction().replace(R.id.setting_container, new SettingFragment()).commit();
    }
}


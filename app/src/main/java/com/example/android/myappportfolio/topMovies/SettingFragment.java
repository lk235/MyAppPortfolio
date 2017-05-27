package com.example.android.myappportfolio.topMovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.android.myappportfolio.R;

/**
 * Created by lk235 on 2017/5/25.
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

        @Override
    public boolean onPreferenceChange(Preference preference,Object value){
        String stringValue = value.toString();
        ListPreference listPreference = (ListPreference)preference;
        int prefIndex = listPreference.findIndexOfValue(stringValue);
        preference.setSummary(listPreference.getEntries()[prefIndex]);

        return true;
    }
    private void bindPreferenceSummaryToValue(Preference preference){
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(),""));

    }
}

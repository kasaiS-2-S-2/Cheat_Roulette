package com.e.myroulette1;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class DetailSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        //Toolbar toolbar = new Toolbar(getContext());
        //toolbar = findViewById(R.id.toolbar_myRoulette);
        //toolbar.setTitle(getResources().getString(R.string.detail_settings));
        //getActivity().setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getActivity().setTitle(getResources().getString(R.string.detail_settings));

        setPreferencesFromResource(R.xml.detail_settings, rootKey);
    }
}

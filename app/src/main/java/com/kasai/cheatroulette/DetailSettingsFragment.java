package com.kasai.cheatroulette;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class DetailSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getActivity().setTitle(getResources().getString(R.string.detail_settings));

        setPreferencesFromResource(R.xml.detail_settings, rootKey);
    }
}

package com.kasai.cheatroulette.activity.detailSettingActivity;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.kasai.cheatroulette.R;

public class DetailSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getActivity().setTitle(getResources().getString(R.string.detail_settings));

        setPreferencesFromResource(R.xml.detail_settings, rootKey);
    }
}

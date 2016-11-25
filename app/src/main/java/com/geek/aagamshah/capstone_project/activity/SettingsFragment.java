package com.geek.aagamshah.capstone_project.activity;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.geek.aagamshah.capstone_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Preference preference = findPreference("pref_test");
        Intent intent = new Intent(getActivity(),TeleprompterActivity.class);
        intent.putExtra(TypeActivity.EXTRA_TELETEXT,getString(R.string.blind_text));
        preference.setIntent(intent);
    }
}

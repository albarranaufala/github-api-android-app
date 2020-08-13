package com.example.githubuserapi.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.githubuserapi.R

class MyPreferenceFragment: PreferenceFragmentCompat() {
    private lateinit var LANGUAGE: String

    private lateinit var languagePreference : Preference
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        LANGUAGE = resources.getString(R.string.change_language)

        languagePreference = findPreference<Preference>(LANGUAGE) as Preference

        languagePreference.setOnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            true
        }
    }
}
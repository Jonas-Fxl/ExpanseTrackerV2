package dev.wiprojekt.expansetracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.wiprojekt.expansetracker.Buchung.NeueAusgabe
import dev.wiprojekt.expansetracker.R
import dev.wiprojekt.expansetracker.preferences.PrefHelper


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }
}


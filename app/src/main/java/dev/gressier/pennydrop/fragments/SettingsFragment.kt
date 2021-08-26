package dev.gressier.pennydrop.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.navigation.fragment.findNavController
import androidx.preference.DropDownPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.gressier.pennydrop.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<DropDownPreference?>("theme")?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, _ ->
                activity?.recreate()
                true
            }
        findPreference<ListPreference?>("themeMode")?.apply {
            setDefaultValue(MODE_NIGHT_FOLLOW_SYSTEM)
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                setDefaultNightMode(
                    when ("$newValue") {
                        "Light" -> MODE_NIGHT_NO
                        "Dark" -> MODE_NIGHT_YES
                        else -> MODE_NIGHT_FOLLOW_SYSTEM
                    }
                )
                true
            }
        }
        findPreference<Preference?>("credits")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                findNavController().navigate(R.id.aboutFragment)
                true
            }
    }
}
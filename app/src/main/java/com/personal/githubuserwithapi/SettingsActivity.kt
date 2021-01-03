package com.personal.githubuserwithapi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.title = resources.getString(R.string.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    internal class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            enableAlarm()
            changeLanguage()
        }

        private fun enableAlarm() {

            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            val isEnableAlarm: Boolean = sharedPreferences.getBoolean("ALARM", false)
            if (isEnableAlarm) {

            }
            else {

            }
        }

        private fun changeLanguage() {
            findPreference<Preference>("LANGUAGE")?.setOnPreferenceClickListener {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }
        }

        override fun onResume() {
            enableAlarm()
            super.onResume()
        }
    }
}
package dev.wiprojekt.expansetracker.preferences

import android.content.Context
import androidx.preference.PreferenceManager

const val ITEM_TYPE_KEY = "item_type_key"

class PrefHelper {

    companion object {

        lateinit var currency : String

        fun loadSettings(context: Context) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            currency = sp.getString("pre_cur", "EUR")!!
            val loggin = sp.getBoolean("pre_loggin", true)
        }
    }
}
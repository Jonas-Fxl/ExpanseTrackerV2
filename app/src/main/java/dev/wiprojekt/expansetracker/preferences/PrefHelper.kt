package dev.wiprojekt.expansetracker.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import kotlin.properties.Delegates

const val ITEM_TYPE_KEY = "item_type_key"

class PrefHelper {

    companion object {

        lateinit var currency: String
        var loggin by Delegates.notNull<Boolean>()
        lateinit var budget: String

        fun loadSettings(context: Context) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            currency = sp.getString("pre_cur", "EUR")!!
            loggin = sp.getBoolean("pre_angmeldet", false)
            budget = sp.getString("pre_budget", "0").toString()
        }
    }
}
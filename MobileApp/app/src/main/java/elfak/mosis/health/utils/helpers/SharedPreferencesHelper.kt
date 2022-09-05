package elfak.mosis.health.utils.helpers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SharedPreferencesHelper {

    val STEP_COUNT = "STEP_COUNT"
    val FIRST_TIME = "FIRST_TIME"
    val _ID = "_ID"
    val AVG_PULSE = "AVG_PULSE"
    val MAX_PULSE = "MAX_PULSE"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editPref(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        val value = pair.second
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }

    var SharedPreferences.stepCount
        get() = getInt(STEP_COUNT, 0)
        set(value) {
            editPref { it.putInt(STEP_COUNT, value) }
        }

    var SharedPreferences.firstTime
        get() = getBoolean(FIRST_TIME, true)
        set(value){
            editPref { it.putBoolean(FIRST_TIME, value) }
        }

    var SharedPreferences._id
        get() = getInt(_ID, 0)
        set(value){
            editPref { it.putInt(_ID, value) }
        }

    var SharedPreferences.max_pulse
        get() = getFloat(MAX_PULSE, 0.0f)
        set(value){
            editPref { it.putFloat(MAX_PULSE, value)}
        }

    var SharedPreferences.avg_pulse
        get() = getFloat(AVG_PULSE, 0.0f)
        set(value){
            editPref { it.putFloat(AVG_PULSE, value)}
        }


}
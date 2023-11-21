package edu.uw.ischool.uw2065357.droidquiz

import android.content.Context

class AppPreferences private constructor(context: Context) {

    private val sharedPrefs = context.getSharedPreferences("AppPreference", Context.MODE_PRIVATE)
    private val editor = sharedPrefs.edit()

    // Define default values
    private val defaultUrl = "https://tednewardsandbox.site44.com/questions.json"
    private val defaultMinutes = 60

    // Getter functions with default values
    fun getUrl(): String {
        return sharedPrefs.getString("url", defaultUrl) ?: defaultUrl
    }

    fun getMinutes(): Int {
        return sharedPrefs.getInt("minutes", defaultMinutes)
    }

    // Setter functions
    fun setUrl(url: String) {
        editor.putString("url", url)
        editor.apply()
    }

    fun setMinutes(minutes: Int) {
        editor.putInt("minutes", minutes)
        editor.apply()
    }

    companion object {
        private var instance: AppPreferences? = null

        // Initialize AppPreferences with a context
        fun initialize(context: Context) {
            if (instance == null) {
                instance = AppPreferences(context)
            }
        }

        // Get the instance of AppPreferences
        fun getInstance(): AppPreferences {
            return requireNotNull(instance) { "AppPreferences must be initialized" }
        }
    }
}

package edu.uw.ischool.uw2065357.droidquiz
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.JobIntentService
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

//The DownloadService is a background service that is responsible for attempting to download a
// JSON file from a specified URL. It is designed to be run periodically, in the background.


class DownloadService : JobIntentService() {

    companion object {
        const val ACTION_DOWNLOAD_COMPLETE = "ACTION_DOWNLOAD_COMPLETE"
        const val EXTRA_JSON_DATA = "EXTRA_JSON_DATA"
        const val ACTION_DOWNLOAD_FAILURE = "ACTION_DOWNLOAD_FAILURE"
        const val EXTRA_FAILURE_MESSAGE = "EXTRA_FAILURE_MESSAGE"

        private const val JOB_ID = 1000

        // Enqueue work to the specified JobIntentService
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, DownloadService::class.java, JOB_ID, work)
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onHandleWork(intent: Intent) {
        // Check if the device is connected to the internet
        if (isConnectedToInternet(this)) {
            // Retrieve URL from preferences
            val appPreferences = AppPreferences.getInstance()
            val url = appPreferences.getUrl()


            try {
                // Attempt to download JSON from the specified URL
                val json = downloadJsonFromUrl(url)

                saveToFile(json)

                // Broadcast the downloaded JSON
                val broadcastIntent = Intent(ACTION_DOWNLOAD_COMPLETE)
                broadcastIntent.putExtra(EXTRA_JSON_DATA, json)
                sendBroadcast(broadcastIntent)

            } catch (e: IOException) {
                // Handle download failure
                Log.e("DownloadService", "Error downloading JSON: ${e.message}")

                // Broadcast a failure message
                val failureIntent = Intent(ACTION_DOWNLOAD_FAILURE)
                failureIntent.putExtra(EXTRA_FAILURE_MESSAGE, "Download failed: ${e.message}")
                sendBroadcast(failureIntent)
            }
        } else {
            // Handle no internet connection
            Log.d("DownloadService", "No internet connection")

            // Broadcast a no internet message
            val noInternetIntent = Intent(ACTION_DOWNLOAD_FAILURE)
            noInternetIntent.putExtra(EXTRA_FAILURE_MESSAGE, "No internet connection")
            sendBroadcast(noInternetIntent)
        }
    }

    private fun downloadJsonFromUrl(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        return try {
            // Read the JSON from the input stream
            val inputStream = connection.inputStream
            inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    private fun saveToFile(jsonString: String) {
        try {
            val fileOutputStream = openFileOutput("questions.json", Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            // Broadcast a failure message
            val failureIntent = Intent(ACTION_DOWNLOAD_FAILURE)
            failureIntent.putExtra(EXTRA_FAILURE_MESSAGE, "Download failed: ${e.message}")
            sendBroadcast(failureIntent)
        }
    }

    private fun isConnectedToInternet(context: Context): Boolean {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }

            return result
    }
}

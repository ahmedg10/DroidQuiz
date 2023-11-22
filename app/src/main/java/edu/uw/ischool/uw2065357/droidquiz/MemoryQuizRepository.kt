package edu.uw.ischool.uw2065357.droidquiz

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray

interface DataLoadedListener {
    fun onDataLoaded()
}
class MemoryQuizRepository(context: Context) : QuizTopicRepository {
    private var topics: MutableList<QuizData> = mutableListOf()
    private var dataLoadedListener: DataLoadedListener? = null

    init {
        Log.d("TopicRepoDebug", "Intalizing Topic Repo")
        AppPreferences.initialize(context)
        val appPreferences = AppPreferences.getInstance()
        val savedUrl = appPreferences.getUrl()

        pullJsonFromUrl(savedUrl)

        Log.d("Topics Populated" , "${getAllTopics()}")


    }

    fun setDataLoadedListener(listener: DataLoadedListener) {
        this.dataLoadedListener = listener
    }

    override fun pullJsonFromUrl(url: String) {
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                val inputStream: InputStream = connection.inputStream
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                try {
                    topics.clear()
                    readJson(jsonString)
                    } catch (e: JSONException) {
                        Log.e(TAG, "JSON invalid: $e")
                    }

                }catch (e: IOException) {
                // Handle IOException
                // For example:
                Log.e(TAG, "Error reading from the URL: $e")
            }
        }
    }

    private fun readJson(json: String) {
        val updateList = mutableListOf<QuizData>()
        val updateNameList = mutableListOf<String>()

        try {
            Log.d("JsonParsing", "Start parsing JSON: $json")

            val jsonArray = JSONArray(json)
            Log.d("JsonParsing", "JSON Array length: ${jsonArray.length()}")

            for (i in 0 until jsonArray.length()) {
                val topicObj = jsonArray.getJSONObject(i)

                Log.d("JsonParsing", "Processing JSON object at index $i: $topicObj")

                val quizTitle = topicObj.getString("title")
                val quizShortOverview = topicObj.getString("desc")
                val quizLongOverview = "" // You can modify this based on your data structure

                Log.d("JsonParsing", "Quiz Title: $quizTitle")
                Log.d("JsonParsing", "Short Overview: $quizShortOverview")
                Log.d("JsonParsing", "Long Overview: $quizLongOverview")

                val questionsArray = topicObj.getJSONArray("questions")
                val questions = mutableListOf<Question>()

                Log.d("JsonParsing", "Questions Array length: ${questionsArray.length()}")

                for (j in 0 until questionsArray.length()) {
                    val questionObject = questionsArray.getJSONObject(j)

                    Log.d("JsonParsing", "Processing question at index $j: $questionObject")

                    val text = questionObject.getString("text")
                    val answersArray = questionObject.getJSONArray("answers")
                    val options = mutableListOf<String>()

                    Log.d("JsonParsing", "Question Text: $text")

                    Log.d("JsonParsing", "Answers Array length: ${answersArray.length()}")

                    for (k in 0 until answersArray.length()) {
                        val answer = answersArray.getString(k)
                        Log.d("JsonParsing", "Answer at index $k: $answer")
                        options.add(answer)
                    }

                    val correctAnswer = questionObject.getInt("answer") - 1 // Assuming the answer index is 1-based

                    Log.d("JsonParsing", "Correct Answer Index: $correctAnswer")

                    val question = Question(text, options, correctAnswer)
                    questions.add(question)

                    Log.d("JsonParsing", "Question processed: $question")
                }

                updateNameList.add(quizTitle)

                val quizData = QuizData(quizTitle, questions, quizShortOverview, quizLongOverview)
                updateList.add(quizData)

                Log.d("JsonParsing", "Quiz Data processed: $quizData")
            }
        } catch (e: JSONException) {
            // Log the error if JSON parsing fails
            Log.e("JsonParsingError", "Error parsing JSON: ${e.message}")
        }

        // After parsing and updating the topics list
        Handler(Looper.getMainLooper()).post {
            topics = updateList
            dataLoadedListener?.onDataLoaded()
            Log.d("Topics Populated", "${getAllTopics()}")
        }
        Log.d("JsonParsing", "Update List ${updateList}")

        Log.d("JsonParsing"," The Topics: ${topics}")

        Log.d("JsonParsing", "End parsing JSON. Total Quiz Data processed: ${topics.size}")
    }





    override fun getAllTopics(): List<QuizData> {
        return topics
    }

    override fun getTopicByTitle(topicTitle: String): QuizData? {
        return topics.find { it.quizTitle == topicTitle }
    }

    override fun addTopic(topic: QuizData) {
        topics.add(topic)
    }

    override fun updateTopic(topic: QuizData) {
        val index = topics.indexOfFirst { it.quizTitle == topic.quizTitle }
        if (index != -1) {
            topics[index] = topic
        }
    }

    override fun deleteTopic(topicTitle: String) {
        topics.removeIf { it.quizTitle == topicTitle }
    }
}




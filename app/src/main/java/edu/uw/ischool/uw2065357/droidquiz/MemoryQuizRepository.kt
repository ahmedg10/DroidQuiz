package edu.uw.ischool.uw2065357.droidquiz

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
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

class MemoryQuizRepository(private val context: Context) : QuizTopicRepository {
    private val topics: MutableList<QuizData> = mutableListOf()

    init {
        Log.d("TopicRepoDebug", "Intalizing Topic Repo")

        val appPreferences = AppPreferences.getInstance()
        val savedUrl = appPreferences.getUrl()
        val savedMinutes = appPreferences.getMinutes()

        pullJsonFromUrl(savedUrl)

        Log.d("Checking if Topics Populated" , "${getAllTopics()}")


    }


    private fun pullJsonFromUrl(url: String) {
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
        val executor: Executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val jsonArray = JSONArray(json)

                for (index in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(index)

                    val title = jsonObject.getString("title")
                    val shortOverview = jsonObject.getString("desc")

                    val questionsArray = jsonObject.getJSONArray("questions")
                    val questions = mutableListOf<Question>()

                    for (qIndex in 0 until questionsArray.length()) {
                        val questionObject = questionsArray.getJSONObject(qIndex)
                        val text = questionObject.getString("text")
                        val answersArray = questionObject.getJSONArray("answers")
                        val answers = mutableListOf<String>()

                        for (aIndex in 0 until answersArray.length()) {
                            answers.add(answersArray.getString(aIndex))
                        }

                        val correctAnswer = questionObject.getInt("answer")

                        val question = Question(text, answers, correctAnswer)
                        questions.add(question)
                    }

                    val longOverview = "" // You might want to modify this based on your data

                    val quizData = QuizData(title, questions, shortOverview, longOverview)
                    topics.add(quizData)

                    // Inside your pullJsonFromUrl function, after updating topics
                    val quizDataList = topics.toList()  // Create a copy to avoid concurrent modification
                    Log.d("TopicsAfterPull", "Topics after pull: $quizDataList")

                    // Log the processing steps
                    Log.d("QuizProcessing", "Quiz $index processed: $quizData")
                }
            } catch (e: JSONException) {
                // Log the error if JSON parsing fails
                Log.e("JsonParsingError", "Error parsing JSON: ${e.message}")
            }
        }
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




package edu.uw.ischool.uw2065357.droidquiz

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class MemoryQuizRepository(private val context: Context) : QuizTopicRepository {
    private val topics: MutableList<QuizData> = mutableListOf()
    private val fileName = "questions_copy.json"

    init {
        readJsonFile()
    }

    private fun readJsonFile() {
        val file = File(context.filesDir, fileName)

        if (!file.exists()) {
            logError("File not found", file)
            return
        }

        try {
            logDebug("Reading file", file.absolutePath)

            val inputStream = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))

            val stringBuilder = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            inputStream.close()

            val jsonString = stringBuilder.toString()
            logDebug("JSON contents", jsonString) // Add this line to log JSON contents
            topics.addAll(Gson().fromJson(jsonString, object : TypeToken<List<QuizData>>() {}.type))
        } catch (e: Exception) {
            logError("Error reading file", file, e)
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

    private fun logDebug(message: String, filePath: String) {
        Log.d("FileDebug", "$message: $filePath")
    }

    private fun logError(message: String, file: File, exception: Exception? = null) {
        Log.e("FileError", "$message: ${file.absolutePath}", exception)
    }

}

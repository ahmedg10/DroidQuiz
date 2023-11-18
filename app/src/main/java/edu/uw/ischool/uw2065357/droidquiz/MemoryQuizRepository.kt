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
    private val fileName = "questions.json"

    init {
        readJsonFile()
    }

    private fun readJsonFile() {
            val file = File(context.filesDir, "questions_copy.json")

            logDebug("Reading file", file.absolutePath)

            val inputStream = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))

            val stringBuilder = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            inputStream.close()

            val jsonArray = Gson().fromJson<List<Map<String, Any>>>(stringBuilder.toString(), object : TypeToken<List<Map<String, Any>>>() {}.type)

            for ((index, jsonObject) in jsonArray.withIndex()) {
                logDebug("Processing JSON object $index", Gson().toJson(jsonObject))

                val title = jsonObject["title"] as String
                val questionsArray = jsonObject["questions"] as List<Map<String, Any>>
                val questions = mutableListOf<Question>()

                for ((qIndex, questionObject) in questionsArray.withIndex()) {
                    logDebug("Processing question $qIndex for quiz $index", Gson().toJson(questionObject))

                    val questionText = questionObject["text"] as String
                    val answersArray = questionObject["answers"] as List<String>
                    val correctAnswer = (questionObject["answer"] as String).toInt() // Convert to Int

                    val question = Question(questionText, answersArray, correctAnswer)
                    questions.add(question)
                }

                val shortOverview = jsonObject["desc"] as String
                val longOverview = "" // You might want to modify this based on your data

                val quizData = QuizData(title, questions, shortOverview, longOverview)
                topics.add(quizData)

                logDebug("Quiz $index processed", Gson().toJson(quizData))
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

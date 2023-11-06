package edu.uw.ischool.uw2065357.droidquiz

import com.google.gson.Gson

data class QuizData(
    val quizTitle: String,
    val quizQuestions: List<Question>,
    val quizOverview: String
)

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: Int
)

fun QuizData.toJson(): String = Gson().toJson(this)

// Function to convert a JSON string to a QuizData object
fun String.toQuizData(): QuizData = Gson().fromJson(this, QuizData::class.java)

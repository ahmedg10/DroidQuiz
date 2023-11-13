package edu.uw.ischool.uw2065357.droidquiz

interface QuizTopicRepository {
    fun getAllTopics(): List<QuizData>
    fun getTopicByTitle(topicTitle: String): QuizData?
    fun addTopic(topic: QuizData)
    fun updateTopic(topic: QuizData)
    fun deleteTopic(topicId: String)
}
package edu.uw.ischool.uw2065357.droidquiz

class MemoryQuizRepository: QuizTopicRepository {
    private val topics: MutableList<QuizData> = mutableListOf()

    init {
        initializeMockData()
    }

    private fun initializeMockData() {
        topics.addAll(QuizDataMock.quizDataList)
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
        topics.removeIf { it.quizTitle == topicTitle}
    }
}
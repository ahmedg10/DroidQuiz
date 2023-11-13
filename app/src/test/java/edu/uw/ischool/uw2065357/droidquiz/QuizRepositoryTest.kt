package edu.uw.ischool.uw2065357.droidquiz

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class QuizRepositoryTest {

    @Test
    fun addAndGetTopic() {
        val repository = MemoryQuizRepository()

        val newTopic = QuizData("New Topic", emptyList(), "Short Overview", "Long Overview")
        repository.addTopic(newTopic)

        val retrievedTopic = repository.getTopicByTitle("New Topic")

        assertEquals(newTopic, retrievedTopic)
    }

    @Test
    fun getNonexistentTopic() {
        val repository = MemoryQuizRepository()

        val retrievedTopic = repository.getTopicByTitle("Nonexistent Topic")

        assertNull(retrievedTopic)
    }

    @Test
    fun getAllTopics() {
        val repository = MemoryQuizRepository()

        val expectedTopics = QuizDataMock.quizDataList
        val actualTopics = repository.getAllTopics()

        assertEquals(expectedTopics.size, actualTopics.size)
        assertTrue(actualTopics.containsAll(expectedTopics))
    }

}

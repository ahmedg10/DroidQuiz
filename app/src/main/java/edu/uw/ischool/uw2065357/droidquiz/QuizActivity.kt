package edu.uw.ischool.uw2065357.droidquiz


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.uw.ischool.uw2065357.droidquiz.QuizData
import edu.uw.ischool.uw2065357.droidquiz.R

class QuizActivity : AppCompatActivity() {
    private lateinit var quizRepository: QuizTopicRepository
    private var quizData: QuizData? = null
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private var selectedAnswer: Int = -1 // Initialize with an invalid value
    private var currentTopicIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val nextButton = findViewById<Button>(R.id.nextButton)
        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.visibility = View.INVISIBLE
        val backButton = findViewById<Button>(R.id.backButton)
        val correctAnswerNumTextView = findViewById<TextView>(R.id.num_correct)

        correctAnswerNumTextView.text = "$correctAnswers out of $currentQuestionIndex correct!"


        quizRepository = MemoryQuizRepository()

        // Retrieve the selected topic identifier (e.g., quiz title) from the intent
        val selectedTopicTitle = intent.getStringExtra("selectedTopic")

        // Find the index of the selected topic in the repository
        val selectedTopicIndex = quizRepository.getAllTopics().indexOfFirst { it.quizTitle == selectedTopicTitle }

        if (selectedTopicIndex != -1) {
            currentTopicIndex = selectedTopicIndex
            quizData = quizRepository.getAllTopics()[currentTopicIndex]
            loadQuestion()

            checkRadioButtonSelection()

            nextButton.setOnClickListener{
                navigateToNextQuestion()
            }

            backButton.setOnClickListener{
                handleBackButton()
            }

            // ... rest of your existing code
        } else {
            // Handle the case where the selected topic is not found
            showToast("Selected topic not found!")
            finish() // Finish the activity or navigate to another appropriate screen
        }


    }

    private fun handleBackButton() {
        if (currentQuestionIndex > 0) {
            // If we're not on the first question, go back to the previous question
            currentQuestionIndex--
            loadQuestion()
        } else {
            // If we're on the first question, go back to the topic list page
            navigateToMainActivity()
        }
    }

    private fun loadQuestion() {
        val nextButton = findViewById<Button>(R.id.nextButton)

        if (currentQuestionIndex < quizData?.quizQuestions?.size ?: 0) {
            val question = quizData?.quizQuestions?.get(currentQuestionIndex)
            val questionTextView = findViewById<TextView>(R.id.question)
            questionTextView.text = question?.text // Set the question text

            val answerChoice1 = findViewById<RadioButton>(R.id.answer_choice_1)
            answerChoice1.text = question?.options?.get(0) // Set answer choice 1 text

            val answerChoice2 = findViewById<RadioButton>(R.id.answer_choice_2)
            answerChoice2.text = question?.options?.get(1) // Set answer choice 2 text

            val answerChoice3 = findViewById<RadioButton>(R.id.answer_choice_3)
            answerChoice3.text = question?.options?.get(2) // Set answer choice 3 text

            // Set up click listeners for radio buttons
            answerChoice1.setOnClickListener { handleRadioButtonClick(0) }
            answerChoice2.setOnClickListener { handleRadioButtonClick(1) }
            answerChoice3.setOnClickListener { handleRadioButtonClick(2) }

            // Check if it's the last question and update the "Next" button text
            if (currentQuestionIndex == quizData?.quizQuestions?.size?.minus(1)) {
                nextButton.text = "Finish"
                nextButton.setOnClickListener {
                    // Navigate to the main activity on the first click
                    navigateToMainActivity()
                }
            } else {
                nextButton.text = "Next"
                nextButton.setOnClickListener {
                    // Move to the next question
                    navigateToNextQuestion()
                }
            }
        }
    }


    fun handleRadioButtonClick(int: Int){
        this.selectedAnswer = selectedAnswer
    }

    private fun showAnswer(selectedAnswer: Int) {
        val question = quizData?.quizQuestions?.get(currentQuestionIndex)

        if (question?.correctAnswer == selectedAnswer) {
            // The selected answer is correct
            showToast("Correct answer!")
            correctAnswers++
        } else {
            // The selected answer is incorrect
            showToast("Incorrect answer!")
        }

        // Update the score display
        val correctAnswerNumTextView = findViewById<TextView>(R.id.num_correct)
        correctAnswerNumTextView.text = "$correctAnswers out of ${currentQuestionIndex + 1} correct!"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToNextQuestion() {
        if (currentQuestionIndex < quizData?.quizQuestions?.size ?: 0) {
            // Deselect all radio buttons
            val radioGroup = findViewById<RadioGroup>(R.id.answer_group)
            radioGroup.clearCheck()

            // Reset the background of all radio buttons to transparent
            for (i in 0 until radioGroup.childCount) {
                val radioButton = radioGroup.getChildAt(i) as RadioButton
                radioButton.setBackgroundResource(android.R.color.transparent)
            }
            currentQuestionIndex++
            loadQuestion()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun highlightAnswers() {
        val radioGroup = findViewById<RadioGroup>(R.id.answer_group)
        val correctAnswer = quizData?.quizQuestions?.get(currentQuestionIndex)?.correctAnswer
        val selectedRadioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)

        val correctRadio = radioGroup.getChildAt(correctAnswer ?: -1) as RadioButton
        val selectedRadio = radioGroup.getChildAt(radioGroup.indexOfChild(selectedRadioButton)) as RadioButton

        correctRadio.setBackgroundResource(R.drawable.correct_background)

        if (correctAnswer != null && selectedRadio.id != correctRadio.id) {
            // Selected answer is incorrect
            selectedRadio.setBackgroundResource(R.drawable.incorrect_background)
        }
    }



        // Check if a radio button is selected and show "Submit" button
        private fun checkRadioButtonSelection() {
            val radioGroup = findViewById<RadioGroup>(R.id.answer_group)
            val submitButton = findViewById<Button>(R.id.submitButton)

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId != -1) {
                    submitButton.visibility = View.VISIBLE
                }
            }

            submitButton.setOnClickListener {
                val selectedRadioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                val indexOfSelectedAnswer = radioGroup.indexOfChild(selectedRadioButton)
                if (selectedRadioButton != null) {
                    highlightAnswers()
                    showAnswer(indexOfSelectedAnswer)
                } else {
                    // Display an error message or do nothing if no radio button is selected
                }
            }
        }

}



package edu.uw.ischool.uw2065357.droidquiz

object QuizDataMock {
    val quizDataList = listOf(
        QuizData(
            "Math",
            listOf(
                Question("What is 2 + 2?", listOf("3", "4", "5"), 1),
                Question("What is 5 * 7?", listOf("30", "35", "42"), 2),
                // Add more questions
            ),
            "This is a Math Quiz!"
        ),
        QuizData(
            "Physics",
            listOf(
                Question("What is Newton's first law of motion?", listOf("Inertia", "Action and reaction", "Force"), 0),
                Question("What is the SI unit of force?", listOf("Newton", "Joule", "Watt"), 0),
                // Add more questions
            ),
            "This is a quiz to see if you are a physics guy"
        ),
        QuizData(
            "Marvel Super Heroes",
            listOf(
                Question("Who is the strongest Avenger?", listOf("Iron Man", "Thor", "Hulk"), 2),
                Question("What is the real name of Spider-Man?", listOf("Peter Parker", "Tony Stark", "Bruce Banner"), 0),
                // Add more questions for Marvel Super Heroes
            ),
            "Do you really know Marvel?"
        )
    )
}
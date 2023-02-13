package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

const val IS_NEW_PROBLEM_REMAINING = "is_new_problem_remaining"
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(
            R.string.question_australia,
            listOf(
                Answer(R.string.australia_0_answer, true),
                Answer(R.string.australia_1_answer, false),
                Answer(R.string.australia_2_answer, false),
                Answer(R.string.australia_3_answer, false)
            )
        ),
        Question(R.string.oceanQuestion,
            listOf(
                Answer(R.string.ocean_0_answer,false),
                Answer(R.string.ocean_1_answer,true),
                Answer(R.string.ocean_2_answer,false),
                Answer(R.string.ocean_3_answer,false)
            )),
        Question(R.string.landSizeQuestion,
            listOf(
                Answer(R.string.landSize_0_answer,false),
                Answer(R.string.landSize_1_answer,false),
                Answer(R.string.landSize_2_answer,true),
                Answer(R.string.landSize_3_answer,false)
            )),
        Question(R.string.richestQuestion,
            listOf(
                Answer(R.string.richest_0_answer,false),
                Answer(R.string.richest_1_answer,false),
                Answer(R.string.richest_2_answer,false),
                Answer(R.string.richest_3_answer,true)
            )
        )
    )
    var questionIndex = 0

    var correctAnswers = 0
    val totalQuestions = questionBank.size
    var hintsUsed =0
    //val questionText get() = questionBank[questionIndex].textResId
    val answerList get() = questionBank[questionIndex].answer

    var newProblemRemaining :Boolean
        get() = savedStateHandle.get(IS_NEW_PROBLEM_REMAINING) ?: true
        set(value) = savedStateHandle.set(IS_NEW_PROBLEM_REMAINING, value)

    val question
        get() = questionBank[questionIndex].textResId

    val answer
        get() = questionBank[questionIndex].answer

    fun gotoNextQuestion() {
        questionIndex = (questionIndex + 1) % questionBank.size
    }

    fun countCorrect() {
        var count = 0
        questionBank.forEach {
            it.answer.forEach { answer->
                if (answer.isSelected)
                    count++
            }
        }
        correctAnswers = count
    }
    fun giveHint() {
        val hideAnswer = answer
            .filter { !it.isCorrect }
            .filter { it.isEnabled }
            .random()
        hideAnswer.isEnabled = false
        hideAnswer.isSelected = false
        hintsUsed ++
    }

    fun lastQuestion(): Boolean {
        val result = questionIndex == questionBank.size-1
        if (result)
            newProblemRemaining = false
        return result
    }

    fun resetAll() {
        if(!newProblemRemaining)
            return
            correctAnswers = 0
            hintsUsed = 0
        questionBank.forEach {
            it.answer.forEach { answer ->
                answer.isEnabled = true
                answer.isSelected = false
            }
        }
    }
}

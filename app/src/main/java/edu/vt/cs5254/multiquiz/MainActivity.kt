package edu.vt.cs5254.multiquiz

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import kotlin.collections.forEach
import androidx.activity.viewModels

import edu.vt.cs5254.multiquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //James Smith
    //PID - jfs8888

    private lateinit var binding: ActivityMainBinding
    private val multiQuizModel: QuizViewModel by viewModels()
    lateinit var answerButtonList: List<Button>

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        multiQuizModel.goToNextQuestion()
        if (result.resultCode == Activity.RESULT_OK) {
            multiQuizModel.newProblemRemaining = result.data?.getBooleanExtra(EXTRA_RESET_ALL, false)?:false
        }
        multiQuizModel.resetAll()
        updateProblem()
        updateButtons()
    }

    //required
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        answerButtonList = listOf(
            binding.answer0Button,
            binding.answer1Button,
            binding.answer2Button,
            binding.answer3Button
        )
        updateButtons()
        updateProblem()
        }

    private fun updateProblem(){
        binding.questionTextView.setText(multiQuizModel.question);
        answerButtonList.zip(multiQuizModel.answerList).forEach {
                (button,answer) -> button.setText(answer.textResId)
        }
        answerButtonList.zip(multiQuizModel.answerList).forEach{
                (button,answer)->
            button.setOnClickListener {
                answerClickedEvent(answer)
            }
        }
        binding.hintButton.setOnClickListener {
            hintClickedEvent()
        }
        binding.submitButton.setOnClickListener {
            submitClickedEvent()
        }
    }

    private fun answerClickedEvent(answer: Answer){
        answer.isSelected = !answer.isSelected
        multiQuizModel.answerList.filter { it!=answer}.forEach{it.isSelected=false}
        updateButtons()
    }

    private fun hintClickedEvent(){
        multiQuizModel.giveHint()
        updateButtons()
    }

    private fun submitClickedEvent(){
        if (multiQuizModel.answerList.any {
            it -> it.isSelected && it.isCorrect
            }) {
            multiQuizModel.countCorrect()
        }
        if(multiQuizModel.lastQuestion()){
            goToResultActivity()
        }else{
            multiQuizModel.goToNextQuestion()
            updateProblem()
            updateButtons()
        }
    }

    private fun goToResultActivity(){
        val intent = ResultsActivity.newIntent(
            this@MainActivity,
            multiQuizModel.correctAnswers,
            multiQuizModel.totalQuestions,
            multiQuizModel.hintsUsed
        )
        resultLauncher.launch(intent)
    }

    private fun updateButtons(){
        updateAnswerButtons()
        updateHintButton()
        updateSubmitButton()
    }

    //update the button status,based on the answer states
    private fun updateAnswerButtons(){
        answerButtonList.zip(multiQuizModel.answerList).forEach {
                (button,answer) ->
            button.isSelected = answer.isSelected
            button.isEnabled = answer.isEnabled
            button.updateColor()
        }

    }

    private fun updateSubmitButton(){
        binding.submitButton.isEnabled = multiQuizModel.answerList.any {
            it.isSelected
        }
    }

    private fun updateHintButton(){
        binding.hintButton.isEnabled =
            multiQuizModel.answerList.filterNot {
                it.isCorrect
            }.any {
                it.isEnabled
            }
    }
}
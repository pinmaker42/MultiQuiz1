package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.vt.cs5254.multiquiz.databinding.ActivityMainBinding
import edu.vt.cs5254.multiquiz.databinding.ActivityResultsBinding

private const val EXTRA_TOTAL_QUESTIONS = "edu.vt.cs5254.multiquiz.submitted_answers"
private const val EXTRA_TOTAL_CORRECT = "edu.vt.cs5254.multiquiz.correct_answers"
private const val EXTRA_TOTAL_HINTS = "edu.vt.cs5254.multiquiz.hints_used"
const val EXTRA_RESET_ALL="edu.vt.cs5254.multiquiz.reset_all"

class ResultsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultsBinding
    private var correctAnswers = 0
    private var totalQuestions = 0
    private var hintsUsed = 0

    private val resultsViewModelkt: ResultsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        totalQuestions = intent.getIntExtra(EXTRA_TOTAL_QUESTIONS, -1)
        correctAnswers = intent.getIntExtra(EXTRA_TOTAL_CORRECT, 0)
        hintsUsed = intent.getIntExtra(EXTRA_TOTAL_HINTS, 0)

        showScore()

        binding.resetAllButton.setOnClickListener {
            resetAllEvent()
        }
    }

    private fun resetAllEvent() {
        binding.resetAllButton.isEnabled = false
        setResetAllResult(true)
    }

    private fun setResetAllResult(resetAllClicked: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_RESET_ALL, resetAllClicked)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun showScore() {
        binding.correctAnswersValue.text = correctAnswers.toString()
        binding.submittedAnswersValue.text = totalQuestions.toString()
        binding.hintsUsedValue.text = hintsUsed.toString()
    }

    companion object {
        fun newIntent(
            packageContext: Context,
            totalQuestions: Int,
            totalCorrect: Int,
            totalHints: Int
        ): Intent {
            return Intent(packageContext, ResultsActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_QUESTIONS, totalQuestions)
                putExtra(EXTRA_TOTAL_CORRECT, totalCorrect)
                putExtra(EXTRA_TOTAL_HINTS, totalHints)
            }
        }
    }
}
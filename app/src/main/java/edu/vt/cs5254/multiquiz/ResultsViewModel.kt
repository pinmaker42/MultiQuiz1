package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class ResultsViewModel : ViewModel() {
    private var resetPressed = false
    fun resetClicked() {
        resetPressed = true
    }

    val isResetAvailable
        get() = !resetPressed
}
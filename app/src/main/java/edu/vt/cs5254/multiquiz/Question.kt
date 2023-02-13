package edu.vt.cs5254.multiquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: List<Answer>)
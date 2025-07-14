package com.example.qasystemappcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qasystemappcompose.network.RetrofitClient
import com.example.qasystemappcompose.database.AppDatabase
import com.example.qasystemappcompose.database.QuestionAnswer
import com.example.qasystemappcompose.model.QuestionRecord
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val db: AppDatabase) : ViewModel() {

    private val _questions = MutableStateFlow<List<QuestionRecord>>(emptyList())
    val questions: StateFlow<List<QuestionRecord>> = _questions

    private val _currentQuestion = MutableStateFlow<QuestionRecord?>(null)
    val currentQuestion: StateFlow<QuestionRecord?> = _currentQuestion

    init {
        fetchAll()
    }

    // ✅ fetch JSON from API
    fun fetchAll() = viewModelScope.launch {
        val response = RetrofitClient.apiService.getQuestions()
        if (response.isSuccessful) {
            _questions.value = response.body()?.record.orEmpty()
            _currentQuestion.value = _questions.value.firstOrNull()
        }
    }

    // ✅ Answer current question and navigate next
    fun answered(answer: String) = viewModelScope.launch {
        _currentQuestion.value?.let { q ->
            db.answerDao().insert(QuestionAnswer(questionId = q.id, answer = answer))
            q.referTo?.id?.let { navigateTo(it) }
        }
    }

    // ✅ Skip current question
    fun skip() = viewModelScope.launch {
        _currentQuestion.value?.skip?.id?.let { id ->
            navigateTo(id)
        }
    }

    // ✅ Navigate to next question or submit screen
    private fun navigateTo(id: String) {
        _currentQuestion.value = if (id == "submit") null
        else _questions.value.find { it.id == id }
    }

    // ✅ Get all saved answers from Room
    fun getAllAnswers(callback: (List<QuestionAnswer>) -> Unit) = viewModelScope.launch {
        callback(db.answerDao().getAll())
    }

    // ✅ Restart survey: clear old answers and reset first question
    fun restartSurvey() = viewModelScope.launch {
        db.answerDao().clear()
        _currentQuestion.value = _questions.value.firstOrNull()
    }

    // ✅ Get Question Text by ID (for ResultScreen)
    fun getQuestionTextById(id: String): String {
        return _questions.value.find { it.id == id }?.question?.slug ?: "Unknown Question"
    }
}
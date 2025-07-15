package com.example.qasystemappcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qasystemappcompose.network.RetrofitClient
import com.example.qasystemappcompose.database.AppDatabase
import com.example.qasystemappcompose.database.QuestionAnswer
import com.example.qasystemappcompose.model.QuestionRecord
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// MainViewModel.kt — updated to support referTo, skip logic, submit, and refresh
class MainViewModel(private val db: AppDatabase) : ViewModel() {

    private val _questions = MutableStateFlow<List<QuestionRecord>>(emptyList())
    val questions: StateFlow<List<QuestionRecord>> = _questions

    private val _currentQuestion = MutableStateFlow<QuestionRecord?>(null)
    val currentQuestion: StateFlow<QuestionRecord?> = _currentQuestion

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val _submitted = MutableStateFlow(false)
    val submitted: StateFlow<Boolean> = _submitted

    init {
        fetchAll()
    }

    // Step 1: Fetch all questions from API
    fun fetchAll() = viewModelScope.launch {
        val response = RetrofitClient.apiService.getQuestions()
        if (response.isSuccessful) {
            val list = response.body()?.record.orEmpty()
            _questions.value = list
            _currentQuestion.value = list.firstOrNull()
            _currentQuestionIndex.value = 0
            _submitted.value = false
        }
    }

    // Step 2: Answer current question and navigate using referTo
    fun answered(answer: String) = viewModelScope.launch {
        _currentQuestion.value?.let { currentQ ->
            db.answerDao().insert(QuestionAnswer(questionId = currentQ.id, answer = answer))

            val nextId = currentQ.referTo?.id
            if (nextId == "submit") {
                _currentQuestion.value = null
                _currentQuestionIndex.value = -1
                _submitted.value = true
            } else {
                val nextQ = _questions.value.find { it.id == nextId }
                _currentQuestion.value = nextQ
                _currentQuestionIndex.value = _questions.value.indexOf(nextQ)
            }
        }
    }

    // Step 3 & 4: Skip to next question using skip ID
    fun skip() = viewModelScope.launch {
        _currentQuestion.value?.let { currentQ ->
            val skipId = currentQ.skip?.id
            if (skipId != null && skipId != "-1") {
                val skipQ = _questions.value.find { it.id == skipId }
                if (skipQ != null) {
                    _currentQuestion.value = skipQ
                    _currentQuestionIndex.value = _questions.value.indexOf(skipQ)
                    return@launch
                }
            }

            val i = _questions.value.indexOf(currentQ)
            val next = i + 1
            if (next in _questions.value.indices) {
                _currentQuestion.value = _questions.value[next]
                _currentQuestionIndex.value = next
            } else {
                _currentQuestion.value = null
                _currentQuestionIndex.value = -1
            }
        }
    }

    // Step 5: Get all answers
    fun getAllAnswers(callback: (List<QuestionAnswer>) -> Unit) = viewModelScope.launch {
        callback(db.answerDao().getAll())
    }

    // Step 6: Restart survey
    fun restartSurvey() = viewModelScope.launch {
        db.answerDao().clear()
        val first = _questions.value.firstOrNull()
        _currentQuestion.value = first
        _currentQuestionIndex.value = if (first != null) 0 else -1
        _submitted.value = false
    }

    // Utility: Get question text by ID
    fun getQuestionTextById(id: String): String {
        return _questions.value.find { it.id == id }?.question?.slug ?: "Unknown Question"
    }

    // Utility: Check if submitted (for navigation)
    fun isSubmitted(): Boolean = submitted.value
} // END
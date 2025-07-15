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

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex


    // Called when the ViewModel is initialized — loads all questions from API
    init {
        fetchAll()
    }

    // ✅ Fetch questions from the API and initialize the first question
    fun fetchAll() = viewModelScope.launch {
        val response = RetrofitClient.apiService.getQuestions()
        if (response.isSuccessful) {
            val list = response.body()?.record.orEmpty()
            _questions.value = list
            _currentQuestion.value = list.firstOrNull()
            _currentQuestionIndex.value = 0
        }
    }

    // ✅ Save answer and move to the next question
    fun answered(answer: String) = viewModelScope.launch {
        _currentQuestion.value?.let { currentQ ->
            db.answerDao().insert(QuestionAnswer(questionId = currentQ.id, answer = answer))

            val nextId = currentQ.referTo?.id
            if (nextId == null || nextId == "submit") {
                _currentQuestion.value = null
                _currentQuestionIndex.value = -1
            } else {
                val nextQuestion = _questions.value.find { it.id == nextId }
                _currentQuestion.value = nextQuestion
                _currentQuestionIndex.value = _questions.value.indexOf(nextQuestion)
            }
        }
    }


    // ✅ Skip the current question
    fun skip() = viewModelScope.launch {
        _currentQuestion.value?.let { currentQ ->
            val skipId = currentQ.skip?.id

            if (skipId != null) {
                val skipQuestion = _questions.value.find { it.id == skipId }
                if (skipQuestion != null) {
                    _currentQuestion.value = skipQuestion
                    _currentQuestionIndex.value = _questions.value.indexOf(skipQuestion)
                    return@launch
                }
            }

            //  skip
            val currentIndex = _questions.value.indexOf(currentQ)
            val nextIndex = currentIndex + 1
            if (nextIndex in _questions.value.indices) {
                _currentQuestion.value = _questions.value[nextIndex]
                _currentQuestionIndex.value = nextIndex
            } else {
                _currentQuestion.value = null
                _currentQuestionIndex.value = -1
            }
        }
    }

    // ✅ Retrieve all saved answers and pass them to a callback (used in ResultsScreen)
    fun getAllAnswers(callback: (List<QuestionAnswer>) -> Unit) = viewModelScope.launch {
        callback(db.answerDao().getAll())
    }

    // ✅ Restart the survey — clear answers and reset to the first question
    fun restartSurvey() = viewModelScope.launch {
        db.answerDao().clear()
        val first = _questions.value.firstOrNull()
        _currentQuestion.value = first
        _currentQuestionIndex.value = if (first != null) 0 else -1
    }

    // ✅ Get question text by ID (used to display in results)
    fun getQuestionTextById(id: String): String {
        return _questions.value.find { it.id == id }?.question?.slug ?: "Unknown Question"
    }
}
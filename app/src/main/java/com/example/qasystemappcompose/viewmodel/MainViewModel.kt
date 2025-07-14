package com.example.qasystemappcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qasystemappcompose.network.RetrofitClient
import com.example.qasystemappcompose.model.Question
import com.example.qasystemappcompose.database.AppDatabase
import com.example.qasystemappcompose.database.QuestionAnswer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val db: AppDatabase) : ViewModel() {
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    private val _currentQuestion = MutableStateFlow<Question?>(null)
    val currentQuestion: StateFlow<Question?> = _currentQuestion

    init { fetchAll() }

    fun fetchAll() = viewModelScope.launch {
        val resp = RetrofitClient.apiService.getQuestions()
        if (resp.isSuccessful) {
            _questions.value = resp.body()?.record?.questions.orEmpty()
            _currentQuestion.value = _questions.value.firstOrNull()
        }
    }

    fun answered(answer: String) = viewModelScope.launch {
        _currentQuestion.value?.let { q ->
            db.answerDao().insert(QuestionAnswer(questionId = q.id, answer = answer))
            navigateTo(q.referTo)
        }
    }

    fun skip() = viewModelScope.launch {
        _currentQuestion.value?.let {
            navigateTo(it.skip)
        }
    }

    private fun navigateTo(id: String) = viewModelScope.launch {
        if (id == "submit") _currentQuestion.value = null
        else _questions.value.find { it.id == id }?.let { _currentQuestion.value = it }
    }

    fun getAllAnswers(callback: (List<QuestionAnswer>) -> Unit) = viewModelScope.launch {
        callback(db.answerDao().getAll())
    }
}
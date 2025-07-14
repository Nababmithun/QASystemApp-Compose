package com.example.qasystemappcompose.model

data class Question(
    val id: String,
    val question: String,
    val type: String,
    val referTo: String,
    val skip: String = "-1",
    val options: List<String>? = null
)

package com.example.qasystemappcompose.model


data class APIResponse(
    val record: Record
)

data class Record(
    val questions: List<Question>
)
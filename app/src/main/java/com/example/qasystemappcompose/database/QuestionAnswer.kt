package com.example.qasystemappcompose.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "answers")
data class QuestionAnswer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionId: String,
    val answer: String
)
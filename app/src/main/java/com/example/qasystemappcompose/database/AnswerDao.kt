package com.example.qasystemappcompose.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnswerDao {
    @Insert
    suspend fun insert(answer: QuestionAnswer)

    @Query("SELECT * FROM question_answer")
    suspend fun getAll(): List<QuestionAnswer>
}
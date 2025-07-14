package com.example.qasystemappcompose.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnswerDao {

    @Insert
    suspend fun insert(answer: QuestionAnswer)

    @Query("SELECT * FROM answers")
    suspend fun getAll(): List<QuestionAnswer>

    @Query("DELETE FROM answers")
    suspend fun clear()
}
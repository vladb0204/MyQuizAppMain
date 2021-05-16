package com.education.android.myquizappmain.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.*

@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz")
    fun getQuizes(): LiveData<List<Quiz>>

    @Query("SELECT * FROM quiz WHERE id=(:id)")
    public fun getQuiz(id: UUID): LiveData<Quiz?>

    @Update
    fun updateQuiz(quiz: Quiz)

    @Insert
    public fun addQuiz(quiz: Quiz)
}
package com.education.android.myquizappmain.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "quiz-database"

class QuizRepository private constructor(context: Context) {

    private val database: QuizDatabase = Room.databaseBuilder(context.applicationContext,
        QuizDatabase::class.java, DATABASE_NAME).addMigrations(migration_1_2).build()
    private val quizDao = database.quizDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getQuizes(): LiveData<List<Quiz>> = quizDao.getQuizes()

    fun getQuiz(id: UUID): LiveData<Quiz?> = quizDao.getQuiz(id)

    fun updateQuiz(quiz: Quiz) {
        executor.execute { quizDao.updateQuiz(quiz) }
    }

    fun addQuiz(quiz: Quiz) {
        executor.execute { quizDao.addQuiz(quiz) }
    }

    companion object {
        private var INSTANCE: QuizRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = QuizRepository(context)
            }
        }

        fun get(): QuizRepository {
            return INSTANCE ?: throw IllegalStateException("QuizRepository must be initialized")
        }
    }
}
package com.education.android.myquizappmain.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class QuizDetailViewModel() : ViewModel() {

    private val quizRepository = QuizRepository.get()
    private val quizIdLiveData = MutableLiveData<UUID>()

    val quizLiveData: LiveData<Quiz?> = Transformations.switchMap(quizIdLiveData) { quizId ->
        quizRepository.getQuiz(quizId)
    }

    fun loadQuiz(quizId: UUID) {
        quizIdLiveData.value = quizId
    }

    fun saveQuiz(quiz: Quiz) {
        quizRepository.updateQuiz(quiz)
    }
}
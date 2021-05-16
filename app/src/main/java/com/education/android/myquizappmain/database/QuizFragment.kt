package com.education.android.myquizappmain.database

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.education.android.myquizappmain.database.Quiz
import java.util.*

private const val ARG_QUIZ_ID = "quiz_id"

class QuizFragment : Fragment() {

    private lateinit var quiz: Quiz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quiz = Quiz()
        val quizId: UUID = arguments?.getSerializable(ARG_QUIZ_ID) as UUID
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
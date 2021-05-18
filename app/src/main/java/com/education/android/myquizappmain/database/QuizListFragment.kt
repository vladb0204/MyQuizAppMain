package com.education.android.myquizappmain.database

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.android.myquizappmain.R
import java.util.*

class QuizListFragment : Fragment() {

    private interface Callbacks {
        fun onQuizSelected(quizId: UUID)
    }

    private var callbacks: Callbacks? = null
    private var adapter: QuizAdapter? = QuizAdapter(emptyList())

    private inner class QuizHolder(view: View) :RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var quiz: Quiz

        private val titleTextView: TextView = itemView.findViewById(R.id.quiz_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.quiz_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.solved_image)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(quiz: Quiz) {
            this.quiz = quiz
            titleTextView.text =this.quiz.title
            dateTextView.text = this.quiz.date.toString()
            solvedImageView.visibility = if (quiz.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(view: View?) {
            callbacks?.onQuizSelected(quiz.id)
        }

    }

    private inner class QuizAdapter(var quizes: List<Quiz>) : RecyclerView.Adapter<QuizHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizHolder {
            val view = LayoutInflater.inflate(R.layout.list_item_quiz, parent, this)
        }

        override fun onBindViewHolder(holder: QuizHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }
    }
}
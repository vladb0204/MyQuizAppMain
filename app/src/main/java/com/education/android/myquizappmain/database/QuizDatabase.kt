package com.education.android.myquizappmain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ Quiz::class ], version = 1, exportSchema = true)
@TypeConverters(QuizTypeConverters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao() : QuizDao
}

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Quiz ADD COLUMN needed_column TEXT NOT NULL DEFAULT ''")
    }
}
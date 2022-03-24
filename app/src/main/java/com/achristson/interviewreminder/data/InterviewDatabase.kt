package com.achristson.interviewreminder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.achristson.interviewreminder.models.Interview

@Database(entities = [Interview::class], version = 3, exportSchema = false)
abstract class InterviewDatabase : RoomDatabase() {

    abstract val interviewDao : InterviewDao

    companion object {
        @Volatile
        private var INSTANCE : InterviewDatabase? = null

        fun getInstance(context: Context) : InterviewDatabase {
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        InterviewDatabase::class.java,
                        "interview_database")
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
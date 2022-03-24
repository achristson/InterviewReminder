package com.achristson.interviewreminder.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.achristson.interviewreminder.models.Interview

@Dao
interface InterviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(interview : Interview)

    @Update
    suspend fun update(interview: Interview)

    @Query("select * from interview_table")
    fun getAllInterviews() : LiveData<List<Interview>>

    @Query("select * from interview_table")
    fun getInterviewList() : List<Interview>

    @Query("select * from interview_table where interviewId = :id")
    fun getInterviewWithId(id : Long) : Interview

    @Query("delete from interview_table")
    suspend fun deleteAllInterviews()

    @Query("select * from interview_table order by interviewId desc limit 1")
    suspend fun getRecentInterview() : Interview
}
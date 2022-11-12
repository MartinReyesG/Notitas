package com.codingwithme.notesapp.dao

import androidx.room.*
import com.codingwithme.notesapp.entities.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY id DESC")
    suspend fun getAllTask() : List<Task>

    @Query("SELECT * FROM task WHERE id =:id")
    suspend fun getSpecificTask(id:Int) : Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(task:Task)

    @Delete
    suspend fun deleteTask(task:Task)

    @Query("DELETE FROM task WHERE id =:id")
    suspend fun deleteSpecificTask(id:Int)

    @Update
    suspend fun updateTask(task: Task)
}
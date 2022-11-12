package com.codingwithme.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codingwithme.notesapp.dao.TaskDao
import com.codingwithme.notesapp.entities.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    companion object {
        var taskDatabase: TaskDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): TaskDatabase {
            if (taskDatabase == null) {
                taskDatabase = Room.databaseBuilder(
                    context
                    , TaskDatabase::class.java
                    , "task.db"
                ).build()
            }
            return taskDatabase!!
        }
    }

    abstract fun taskDao():TaskDao
}
package com.developer_harshpatel.demo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.developer_harshpatel.demo.data.dao.HistoryDao
import com.developer_harshpatel.demo.data.model.History

@Database(entities = arrayOf(History::class), version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun getHistoryDao() :HistoryDao
    
    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}
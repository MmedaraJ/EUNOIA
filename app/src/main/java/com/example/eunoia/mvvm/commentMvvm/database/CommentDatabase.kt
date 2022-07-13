package com.example.eunoia.mvvm.commentMvvm.database

import android.content.Context
import androidx.room.*
import com.example.eunoia.mvvm.commentMvvm.dao.CommentDAO
import com.example.eunoia.mvvm.commentMvvm.model.CommentModel

@Database(entities = [CommentModel::class], version = 1, exportSchema = false)
abstract class CommentDatabase : RoomDatabase() {
    abstract fun commentDao() : CommentDAO

    companion object {
        @Volatile
        private var INSTANCE: CommentDatabase? = null

        fun getDatabaseClient(context: Context) : CommentDatabase {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, CommentDatabase::class.java, "COMMENT_DATABASE")
                    .fallbackToDestructiveMigration()
                    .build()
                return INSTANCE!!
            }
        }
    }
}
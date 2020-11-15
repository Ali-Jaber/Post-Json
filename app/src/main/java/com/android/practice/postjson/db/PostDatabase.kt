package com.android.practice.postjson.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.practice.postjson.model.Post

@Database(entities = [Post::class],version = 2,exportSchema = false)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

//    object DatabaseBuilder {
//        private lateinit var INSTANCE: PostDatabase
//
//        fun getDataBase(context: Context): PostDatabase {
//            if (INSTANCE == null) {
//                synchronized(PostDatabase::class.java) {
//                    INSTANCE = Room.databaseBuilder(
//                        context.applicationContext, PostDatabase::class.java, "posts_database"
//                    )
//                        .build()
//                }
//            }
//            return INSTANCE
//        }
//    }
}
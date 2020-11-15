package com.android.practice.postjson.di

import android.app.Application
import androidx.room.Room
import com.android.practice.postjson.db.PostDao
import com.android.practice.postjson.db.PostDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDB(application: Application): PostDatabase {
        return Room.databaseBuilder(
            application, PostDatabase::class.java, "posts_database"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(postDB: PostDatabase): PostDao {
        return postDB.postDao()
    }
}
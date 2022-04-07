package com.kimdo.todoappwithflow.di

import android.app.Application
import androidx.room.Room
import com.kimdo.todoappwithflow.data.TodoDatabase
import com.kimdo.todoappwithflow.data.TodoRepository
import com.kimdo.todoappwithflow.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl( db.dao )
    }
}
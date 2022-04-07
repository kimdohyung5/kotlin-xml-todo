package com.kimdo.todoappwithflow.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("select * from todo where id = :id")
    suspend fun getTodoById(id:Int): Todo?

    @Query("select * from todo")
    fun getTodos(): Flow<List<Todo>>
}
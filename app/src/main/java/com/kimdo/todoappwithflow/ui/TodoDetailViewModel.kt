package com.kimdo.todoappwithflow.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimdo.todoappwithflow.data.Todo
import com.kimdo.todoappwithflow.data.TodoRepository
import com.kimdo.todoappwithflow.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(private val repository: TodoRepository): ViewModel() {

    var todo =  MutableLiveData<Todo?>(null)
    private set

    var title = MutableLiveData<String>("")
    private set

    var description = MutableLiveData<String>("")
    private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun setTodoId(todoId:Int) {
        if( todoId != -1) {
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->

                    Log.d(TAG, "setTodoId: ${todo}")
                    title.value = todo.title
                    description.value = todo.description ?: ""
                    this@TodoDetailViewModel.todo.value = todo
                    sendUiEvent(UiEvent.ValueChange(todo.title, todo.description ?:""))
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when(event) {
            is AddEditTodoEvent.OnTitleChange -> {
                title.value = event.title
            }
            is AddEditTodoEvent.OnDescriptionChange -> {
                description.value = event.description
            }
            is AddEditTodoEvent.OnSavTodoClick -> {

                title.value!!.isBlank()

                viewModelScope.launch {
                    title.value?.let {
                        if(it.isBlank()) {
                            sendUiEvent( UiEvent.ShowSnackbar(
                                message = "The title not empty"
                            ))
                            return@launch
                        }
                    }
                    repository.insertTodo(
                        Todo(
                            title = title.value ?: "",
                            description = description.value ?: "",
                            isDone = todo.value?.isDone ?: false,
                            id = todo.value?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        const val TAG = "TodoDetailViewModel"
    }

}
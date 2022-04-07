package com.kimdo.todoappwithflow.ui

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
class TodoListViewModel @Inject constructor(private val repository: TodoRepository): ViewModel() {

    val todos = repository.getTodos()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTodo: Todo? = null

    fun onEvent(event: TodoListEvent) {
        when( event ) {
            is TodoListEvent.OnRelayToast -> {
                sendUiEvent(UiEvent.ShowSnackbar(event.message))
            }
            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate( event.id))
            }
            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate( -1))
            }
//            else -> Unit
            // 현재는 안쓰지만 향후에는 이런 방식으로 사용을 할ㄹ것이다.
//            is TodoListEvent.OnUndoDeleteClick -> {
//                deletedTodo?.let { todo ->
//                    viewModelScope.launch {
//                        repository.insertTodo(todo)
//                    }
//                }
//            }
//            is TodoListEvent.OnDeleteTodoClick -> {
//                viewModelScope.launch {
//                    deletedTodo = event.todo
//                    repository.deleteTodo(event.todo)
//                    sendUiEvent( UiEvent.ShowSnackbar(
//                        message = "Todo deleted",
//                        action = "Undo"
//                    ))
//                }
//            }
//            is TodoListEvent.OnDoneChange -> {
//                viewModelScope.launch {
//                    repository.insertTodo(
//                        event.todo.copy( isDone = event.isDone )
//                    )
//                }
//            }
        }

    }


    // 내부에서 이벤트를 발생시켜서 처리를 한다.
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}
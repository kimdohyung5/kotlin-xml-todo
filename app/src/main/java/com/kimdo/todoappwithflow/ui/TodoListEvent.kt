package com.kimdo.todoappwithflow.ui



sealed class TodoListEvent {
//    data class OnDeleteTodoClick(val todo: Todo): TodoListEvent()
//    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvent()
//    object OnUndoDeleteClick: TodoListEvent()
    data class OnTodoClick(val id: Int): TodoListEvent()
    object OnAddTodoClick: TodoListEvent()
    data class OnRelayToast(val message:String): TodoListEvent()
}
package com.kimdo.todoappwithflow.ui

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String): AddEditTodoEvent()
    data class OnDescriptionChange(val description: String): AddEditTodoEvent()
    object OnSavTodoClick: AddEditTodoEvent()
}
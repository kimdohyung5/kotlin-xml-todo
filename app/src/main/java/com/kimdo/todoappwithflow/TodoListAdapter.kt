package com.kimdo.todoappwithflow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kimdo.todoappwithflow.data.Todo
import com.kimdo.todoappwithflow.databinding.ListItemBinding

class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {

    private val todos = ArrayList<Todo>()
    private var onTodoItemClickListener: SetOnTodoItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from( parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.binding.textLeft.text = todos[position].title
        holder.binding.textRight.text = todos[position].description

        holder.binding.linearLayout.setOnClickListener {
            onTodoItemClickListener?.click( todos[position].id ?: -1 )
        }
    }

    override fun getItemCount(): Int = todos.size

    fun setTodos(newTodos: List<Todo>) {
        todos.clear()
        todos.addAll( newTodos )
        notifyDataSetChanged()
    }
    fun SetOnListener(listener: SetOnTodoItemClickListener) {
        onTodoItemClickListener = listener
    }

    interface SetOnTodoItemClickListener {
        fun click(id:Int)
    }
    class TodoViewHolder(val binding: ListItemBinding ) : RecyclerView.ViewHolder(binding.root)
    companion object {
        const val TAG = "TodoListAdapter"
    }
}
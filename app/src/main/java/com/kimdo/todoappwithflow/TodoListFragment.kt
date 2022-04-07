package com.kimdo.todoappwithflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kimdo.todoappwithflow.data.Todo
import com.kimdo.todoappwithflow.databinding.FragmentTodoListBinding
import com.kimdo.todoappwithflow.ui.TodoListEvent
import com.kimdo.todoappwithflow.ui.TodoListViewModel
import com.kimdo.todoappwithflow.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var todoAdapter: TodoListAdapter

    private val viewModel:TodoListViewModel by viewModels()
    private lateinit var todos : LiveData<List<Todo>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun populateList() {
        todoAdapter = TodoListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = todoAdapter

        todoAdapter.SetOnListener( object : TodoListAdapter.SetOnTodoItemClickListener{
            override fun click(id: Int) {
                viewModel.onEvent(TodoListEvent.OnTodoClick(id))
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateList()

        todos = viewModel.todos.asLiveData()
        todos.observe(viewLifecycleOwner) { listData ->
          todoAdapter.setTodos( listData )
        }

        binding.btnAdd.setOnClickListener {
            viewModel.onEvent(TodoListEvent.OnAddTodoClick)

        }
        lifecycleScope.launch {
            viewModel.uiEvent.collect { event ->
                when( event) {
                    is UiEvent.ShowSnackbar -> {
                        Toast.makeText(context, " ${event.message} ${event.action}", Toast.LENGTH_SHORT).show()
                    }
                    is UiEvent.Navigate -> {
                        if( event.id == -1) {
                            val action = TodoListFragmentDirections.toDetail(-1)
                            binding.recyclerView.findNavController().navigate( action )
                        } else {
                            val action = TodoListFragmentDirections.toDetail(event.id)
                            binding.recyclerView.findNavController().navigate( action )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
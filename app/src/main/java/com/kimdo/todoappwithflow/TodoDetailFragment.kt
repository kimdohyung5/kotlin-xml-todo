package com.kimdo.todoappwithflow

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.kimdo.todoappwithflow.databinding.FragmentTodoDetailBinding
import com.kimdo.todoappwithflow.ui.AddEditTodoEvent
import com.kimdo.todoappwithflow.ui.TodoDetailViewModel
import com.kimdo.todoappwithflow.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoDetailFragment : Fragment() {

    private var _binding: FragmentTodoDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: TodoDetailFragmentArgs by navArgs()
        Log.d(TAG, "onViewCreated: ${args} ${args.itemId}")

        if( args.itemId != -1) {
            viewModel.setTodoId( args.itemId )
            binding.title.setText( viewModel.title.value)
            binding.description.setText( viewModel.description.value)
        }

        lifecycleScope.launch {
            viewModel.uiEvent.collect { event ->
                when( event ) {
                    is UiEvent.PopBackStack -> {
                        binding.title.findNavController().popBackStack()
                    }
                    is UiEvent.ShowSnackbar -> {
                        Toast.makeText(context, "${event.message} ${event.action}" , Toast.LENGTH_SHORT).show()
                    }
                    is UiEvent.ValueChange -> {
                        binding.title.setText( event.title )
                        binding.description.setText( event.description )
                    }
                    else -> Unit
                }
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.onEvent(AddEditTodoEvent.OnSavTodoClick)
        }

        binding.title.addTextChangedListener { it ->
            viewModel.onEvent(AddEditTodoEvent.OnTitleChange(it.toString()))
        }

        binding.description.addTextChangedListener { it ->
            viewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it.toString()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "TodoDetailFragment"
    }
}
package com.example.softengineeringmessanger.ui.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.softengineeringmessanger.ChatApp
import com.example.softengineeringmessanger.R
import com.example.softengineeringmessanger.databinding.FragmentChatBinding
import com.example.softengineeringmessanger.databinding.FragmentChatListBinding
import com.example.softengineeringmessanger.databinding.FragmentContactsBinding
import com.example.softengineeringmessanger.domain.model.Chat
import com.example.softengineeringmessanger.domain.model.User
import com.example.softengineeringmessanger.ui.chat.ChatViewModel
import com.example.softengineeringmessanger.ui.contacts.ContactsAdapter
import com.example.softengineeringmessanger.ui.contacts.ContactsUiState
import com.example.softengineeringmessanger.ui.contacts.ContactsViewModel
import com.example.softengineeringmessanger.ui.contacts.ContactsViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListFragment : Fragment() {
    @Inject
    lateinit var chatsListViewModelFactory: ChatsListViewModelFactory
    private lateinit var viewModel: ChatsListViewModel

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!
    private val adapter = ChatsAdapter {
        navigateToChat(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as ChatApp).appComponent.inject(this)
        viewModel = ViewModelProvider(this, chatsListViewModelFactory)[ChatsListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvChats.adapter = adapter
        binding.rvChats.layoutManager = LinearLayoutManager(requireContext())
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is ChatsListUiState.Loading -> {
                        showLoadingIndicator()
                    }
                    is ChatsListUiState.Success -> {
                        updateUI(state.chats)
                    }
                    is ChatsListUiState.Error -> {
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoadingIndicator() {
        binding.pbLoading.visibility = View.VISIBLE
        binding.rvChats.visibility = View.GONE
    }

    private fun updateUI(chats: List<Chat>) {
        adapter.submitList(chats)
        binding.rvChats.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        binding.pbLoading.visibility = View.GONE
    }

    private fun observeSearch() {
        binding.btSearch.setOnClickListener {
            /*val query = binding.etSearch.text.toString()
            viewModel.fetchUsers(query)*/
        }
    }

    private fun navigateToChat(it: Int) {

    }
}
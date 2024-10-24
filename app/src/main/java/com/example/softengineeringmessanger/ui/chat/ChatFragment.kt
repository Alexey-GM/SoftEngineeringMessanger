package com.example.softengineeringmessanger.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.softengineeringmessanger.ChatApp
import com.example.softengineeringmessanger.databinding.FragmentChatBinding
import com.example.softengineeringmessanger.domain.model.Message
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatFragment : Fragment() {
    private val args: ChatFragmentArgs by navArgs()
    private var companionId: Int? = null
    private var companionUsername: String? = null

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChatMessagesAdapter

    @Inject
    lateinit var chatViewModelFactory: ChatViewModelFactory
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as ChatApp).appComponent.inject(this)
        viewModel = ViewModelProvider(this, chatViewModelFactory)[ChatViewModel::class.java]
        args.let {
            companionUsername = args.companionUsername
            companionId = args.companionId
            adapter = ChatMessagesAdapter(args.companionId)
            viewModel.fetchMessages(args.companionId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMessages.adapter = adapter
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.tvName.text = companionUsername
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is ChatUiState.Loading -> {
                        showLoadingIndicator()
                    }
                    is ChatUiState.Success -> {
                        updateUI(state.messages)
                    }
                    is ChatUiState.Error -> {
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoadingIndicator() {
        binding.pbLoading.visibility = View.VISIBLE
        binding.rvMessages.visibility = View.GONE
    }

    private fun updateUI(messages: List<Message>) {
        adapter.submitList(messages)
        binding.rvMessages.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        binding.pbLoading.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
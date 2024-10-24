package com.example.softengineeringmessanger.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.softengineeringmessanger.ChatApp
import com.example.softengineeringmessanger.databinding.FragmentContactsBinding
import com.example.softengineeringmessanger.domain.model.User
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsFragment : Fragment() {
    @Inject
    lateinit var contactsViewModelFactory: ContactsViewModelFactory
    private lateinit var viewModel: ContactsViewModel

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val adapter = ContactsAdapter {
        navigateToChat(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as ChatApp).appComponent.inject(this)
        viewModel = ViewModelProvider(this, contactsViewModelFactory)[ContactsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvContacts.adapter = adapter
        binding.rvContacts.layoutManager = LinearLayoutManager(requireContext())
        observeState()
        observeSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is ContactsUiState.Loading -> {
                        showLoadingIndicator()
                    }
                    is ContactsUiState.Success -> {
                        updateUI(state.users)
                    }
                    is ContactsUiState.Error -> {
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoadingIndicator() {
        binding.pbLoading.visibility = View.VISIBLE
        binding.rvContacts.visibility = View.GONE
    }

    private fun updateUI(users: List<User>) {
        adapter.submitList(users)
        binding.rvContacts.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        binding.pbLoading.visibility = View.GONE
    }

    private fun navigateToChat(user: User) {
        val action = ContactsFragmentDirections.actionNavigationContactsToChatFragment(user.id, user.login)
        findNavController().navigate(action)
    }

    private fun observeSearch() {
        binding.btSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            viewModel.fetchUsers(query)
        }
    }
}
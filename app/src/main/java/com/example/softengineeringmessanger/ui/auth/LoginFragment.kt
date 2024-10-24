package com.example.softengineeringmessanger.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.softengineeringmessanger.ChatApp
import com.example.softengineeringmessanger.R
import com.example.softengineeringmessanger.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as ChatApp).appComponent.inject(this)
        viewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btSignIn.setOnClickListener {
            val login = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(login, password)
        }
        lifecycleScope.launch {
            viewModel.loginState.collect { result ->
                result?.let {
                    if (it.isSuccess) {
                        Toast.makeText(requireContext(), it.getOrNull(), Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_navigation_login_to_navigation_chats)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка: ${it.exceptionOrNull()?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



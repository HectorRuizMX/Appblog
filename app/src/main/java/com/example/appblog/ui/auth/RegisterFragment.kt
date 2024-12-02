package com.example.appblog.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appblog.R
import com.example.appblog.core.Result
import com.example.appblog.data.remote.auth.AuthDataSource
import com.example.appblog.databinding.FragmentRegisterBinding
import com.example.appblog.domain.auth.AuthRepoImpl
import com.example.appblog.presentation.auth.AuthViewModel
import com.example.appblog.presentation.auth.AuthViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel> { AuthViewModelFactory(AuthRepoImpl(
            AuthDataSource()
    )) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        signUp()
    }

    private fun signUp() {
        binding.btnSignup.setOnClickListener {

            val username = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()

            if (validateCredentials(password, confirmPassword, username, email)) return@setOnClickListener
            createUser(username,password,email)

            Log.d("signUpData", "data: $username $password $confirmPassword $email ")
        }
    }

    private fun createUser(username: String, password: String, email: String) {
        viewModel.signUp(email,password,username).observe(viewLifecycleOwner, Observer { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignup.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)
                }
                is Result.Failure -> {
                    binding.btnSignup.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun validateCredentials(password: String, confirmPassword: String, username: String, email: String): Boolean {
        if (password != confirmPassword) {
            binding.editTextConfirmPassword.error = "La contraseña no coincide"
            binding.editTextPassword.error = "La contraseña no coincide"
            return true
        }

        if (username.isEmpty()) {
            binding.editTextUsername.error = "La usuario esta vacio"
            return true
        }

        if (email.isEmpty()) {
            binding.editTextEmail.error = "El correo esta vacio"
            return true
        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "La contraseña esta vacia"
            return true
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "La confirmacion de contraseña esta vacia"
            return true
        }
        return false
    }

}
package com.example.appblog.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.appblog.R
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profilePictureImageView: ImageView = view.findViewById(R.id.iv_profile_picture)
        val usernameTextView: TextView = view.findViewById(R.id.tv_username)
        val emailTextView: TextView = view.findViewById(R.id.tv_email)
        val editProfileButton: ImageButton = view.findViewById(R.id.btn_edit_profile)
        val logoutButton: Button = view.findViewById(R.id.btn_logout)

        setProfileData(profilePictureImageView, usernameTextView, emailTextView)

        editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.setupProfileFragment)
        }

        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun setProfileData(profilePictureImageView: ImageView, usernameTextView: TextView, emailTextView: TextView) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val email = user.email
            val username = user.displayName
            val photoUrl = user.photoUrl

            usernameTextView.text = username ?: "Nombre de usuario"
            emailTextView.text = email ?: "correo@example.com"

            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(profilePictureImageView)

        } else {
            Toast.makeText(requireContext(), "No hay usuario logueado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logoutUser() {
        firebaseAuth.signOut()
        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()

        try {
            val navController = findNavController()
            navController.popBackStack(R.id.loginFragment, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
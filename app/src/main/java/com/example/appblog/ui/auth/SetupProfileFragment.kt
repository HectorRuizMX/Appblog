package com.example.appblog.ui.auth

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.appblog.R
import com.example.appblog.core.Result
import com.example.appblog.data.remote.auth.AuthDataSource
import com.example.appblog.databinding.FragmentSetupProfileBinding
import com.example.appblog.domain.auth.AuthRepoImpl
import com.example.appblog.presentation.auth.AuthViewModel
import com.example.appblog.presentation.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }


    private lateinit var binding: FragmentSetupProfileBinding
    private val viewModel by viewModels<AuthViewModel> { AuthViewModelFactory(AuthRepoImpl(
            AuthDataSource()
    )) }
    private val REQUEST_IMAGE_CAPTURE = 1
    private var bitmap: Bitmap? = null

    private fun setProfileData(profilePictureImageView: ImageView, usernameTextView: TextView) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val username = user.displayName
            val photoUrl = user.photoUrl

            usernameTextView.text = username ?: "Nombre de usuario"

            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(profilePictureImageView)

        } else {
            Toast.makeText(requireContext(), "No hay usuario logueado", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)
        binding.profileImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "No se encontro app para abir la camara", Toast.LENGTH_SHORT).show()
            }
        }

        val profilePictureImageView: ImageView = view.findViewById(R.id.profile_image)
        val usernameTextView: TextView = view.findViewById(R.id.text_usuario)

        setProfileData(profilePictureImageView, usernameTextView)

        binding.btnCreateProfile.setOnClickListener {
            val username = binding.textUsuario.text.toString().trim()
            val alertDialog = AlertDialog.Builder(requireContext()).setTitle("Publicando...").create()
            bitmap?.let {
                if(username.isNotEmpty()) {
                    viewModel.updateUserProfile(imageBitmap = it, username = username).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                alertDialog.show()
                            }

                            is Result.Success -> {
                                alertDialog.dismiss()
                                findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                            }

                            is Result.Failure -> {
                                alertDialog.dismiss()
                            }
                        }
                    }
                }
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.profileImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        }
    }

}
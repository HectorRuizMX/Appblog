package com.example.appblog.ui.camera

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.appblog.R
import com.example.appblog.core.Result
import com.example.appblog.data.remote.camera.CameraDataSource
import com.example.appblog.data.remote.home.HomeScreenDataSource
import com.example.appblog.databinding.FragmentCameraBinding
import com.example.appblog.domain.camera.CameraRepoImpl
import com.example.appblog.domain.home.HomeScreenRepoImpl
import com.example.appblog.presentation.camera.CameraViewModel
import com.example.appblog.presentation.camera.CameraViewModelFactory
import com.example.appblog.presentation.home.HomeScreenViewModel
import com.example.appblog.presentation.home.HomeScreenViewModelFactory

@Suppress("DEPRECATION")
class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val REQUEST_IMAGE_CAPTURE = 2
    private var bitmap: Bitmap? = null
    private lateinit var binding:FragmentCameraBinding
    private val viewModel by viewModels<CameraViewModel> {
        CameraViewModelFactory(
                CameraRepoImpl(
                        CameraDataSource()
                )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No se encontró app para abrir la camara", Toast.LENGTH_SHORT).show()
        }

        binding.btnUploadPhoto.setOnClickListener {
            bitmap?.let {
                viewModel.uploadPhoto(it, binding.etxtDescription.text.toString().trim()).observe(viewLifecycleOwner, { result ->
                    when (result) {
                        is Result.Loading -> {
                            Toast.makeText(requireContext(), "Publicando...", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Success -> {
                            findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)
                        }
                        is Result.Failure -> {
                            Toast.makeText(requireContext(), "Error ${result.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.postImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        }
    }

}
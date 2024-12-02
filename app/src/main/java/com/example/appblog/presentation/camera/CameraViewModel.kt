package com.example.appblog.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.appblog.core.Result
import com.example.appblog.domain.auth.AuthRepo
import com.example.appblog.domain.camera.CameraRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class CameraViewModel(private val repo: CameraRepo): ViewModel() {

    fun uploadPhoto(imageBitmap: Bitmap, description: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.uploadPhoto(imageBitmap,description)))
        }catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}

class CameraViewModelFactory(private val repo: CameraRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

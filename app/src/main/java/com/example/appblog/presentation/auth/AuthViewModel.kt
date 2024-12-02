package com.example.appblog.presentation.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.appblog.core.Result
import com.example.appblog.domain.auth.AuthRepo
import kotlinx.coroutines.Dispatchers
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(private val repo: AuthRepo) : ViewModel() {

    fun signIn(correo: String, contrasenia: String) = liveData<Result<FirebaseUser>>(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            val user = repo.signIn(correo, contrasenia)
            if (user != null) {
                emit(Result.Success(user))
            } else {
                emit(Result.Failure(Exception("Ha ocurrido un error al iniciar sesión.")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun signUp(correo: String, contrasenia: String, username: String) = liveData<Result<FirebaseUser>>(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            val user = repo.signUp(correo, contrasenia, username)
            if (user != null) {
                emit(Result.Success(user))
            } else {
                emit(Result.Failure(Exception("Ha ocurrido un error al registrar el usuario.")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun updateUserProfile(imageBitmap: Bitmap, username: String) = liveData<Result<Boolean>>(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            val updateSuccess = repo.updateProfile(imageBitmap, username)
            emit(Result.Success(updateSuccess))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}

class AuthViewModelFactory(private val repo: AuthRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

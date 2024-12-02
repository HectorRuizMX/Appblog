package com.example.appblog.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun signIn(correo: String, contrasenia: String): FirebaseUser?
    suspend fun signUp(correo: String, contrasenia: String, usuario: String): FirebaseUser?
    suspend fun updateProfile(imageBitmap: Bitmap, usuario: String): Boolean
}

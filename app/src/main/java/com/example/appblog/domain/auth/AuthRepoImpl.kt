package com.example.appblog.domain.auth

import android.graphics.Bitmap
import com.example.appblog.data.remote.auth.AuthDataSource
import com.google.firebase.auth.FirebaseUser

class AuthRepoImpl(private val dataSource: AuthDataSource) : AuthRepo {
    override suspend fun signIn(correo: String, contrasenia: String): FirebaseUser? =
        dataSource.signIn(correo, contrasenia)

    override suspend fun signUp(correo: String, contrasenia: String, usuario: String): FirebaseUser? =
        dataSource.signUp(correo, contrasenia, usuario)


    override suspend fun updateProfile(imageBitmap: Bitmap, usuario: String): Boolean {
        return try {
            dataSource.updateUserProfile(imageBitmap, usuario)
            true
        } catch (e: Exception) {
            false
        }
    }
}

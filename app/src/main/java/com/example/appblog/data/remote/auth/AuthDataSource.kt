package com.example.appblog.data.remote.auth

import android.graphics.Bitmap
import android.net.Uri
import com.example.appblog.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream


class AuthDataSource {

    suspend fun signIn(email: String, password: String): FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        return authResult.user
    }

    suspend fun signUp(correo: String, contrasenia: String, usuario: String): FirebaseUser? {
        val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasenia).await()
        authResult.user?.uid?.let { uid ->
            FirebaseFirestore.getInstance().collection("usuarios").document(uid).set(
                User(
                    correo,
                    usuario,
                    "Photo_URL.png"
                )
            )
        }
        return authResult.user
    }

    suspend fun updateUserProfile(imageBitmap: Bitmap, usuario: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val imageRef = FirebaseStorage.getInstance().reference.child("${user?.uid}/profile_picture")
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downloadUrl = imageRef.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(usuario)
            .setPhotoUri(Uri.parse(downloadUrl))
            .build()
        user?.updateProfile(profileUpdates)?.await()
    }


}
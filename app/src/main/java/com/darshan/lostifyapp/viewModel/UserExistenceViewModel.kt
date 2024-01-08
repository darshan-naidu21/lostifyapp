package com.darshan.lostifyapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserExistenceViewModel: ViewModel() {

    val uid = FirebaseAuth.getInstance().currentUser?.uid

    fun isUsernameAvailable(username: String, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val query = db.collection("Lostify Users")
            .whereNotEqualTo(FieldPath.documentId(), uid)
            .whereEqualTo("username", username)

        viewModelScope.launch {
            val result = query.get().await()
            callback(result.isEmpty)
        }
    }

    fun isEmailAvailable(email: String, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val query = db.collection("Lostify Users")
            .whereNotEqualTo(FieldPath.documentId(), uid)
            .whereEqualTo("email", email)

        viewModelScope.launch {
            val result = query.get().await()
            callback(result.isEmpty)
        }
    }

    fun isContactNoAvailable(contactNumber: String, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val query = db.collection("Lostify Users")
            .whereNotEqualTo(FieldPath.documentId(), uid)
            .whereEqualTo("contactNumber", contactNumber)

        viewModelScope.launch {
            val result = query.get().await()
            callback(result.isEmpty)
        }
    }

}
package com.darshan.lostifyapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EmailViewModel: ViewModel() {

    fun isEmailAvailable(email: String, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val query = db.collection("Lostify Users").whereEqualTo("email", email)

        viewModelScope.launch {
            val result = query.get().await()
            callback(result.isEmpty)
        }
    }
}
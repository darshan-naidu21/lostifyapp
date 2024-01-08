package com.darshan.lostifyapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ContactNoViewModel: ViewModel() {

    fun isContactNoAvailable(contactNumber: String, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore
        val query = db.collection("Lostify Users").whereEqualTo("contactNumber", contactNumber)

        viewModelScope.launch {
            val result = query.get().await()
            callback(result.isEmpty)
        }
    }
}
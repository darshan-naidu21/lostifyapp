package com.darshan.lostifyapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darshan.lostifyapp.models.PostedItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostedItemsViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dbLostItems = db.collection("Lost Items")
    private val dbFoundItems = db.collection("Found Items")

    private val _userItems = MutableStateFlow<List<PostedItems>>(emptyList())
    val userItems: StateFlow<List<PostedItems>> = _userItems

    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    init {
        fetchUserItems(uid.toString())
    }

    private fun fetchUserItems(userId: String) {
        viewModelScope.launch {
            val lostItems = fetchLostItems(userId)
            val foundItems = fetchFoundItems(userId)

            _userItems.value = lostItems.plus(foundItems)
        }
    }

    private suspend fun fetchLostItems(userId: String): List<PostedItems> {
        val querySnapshot = dbLostItems.whereEqualTo("userId", userId).get().await()
        val documents = querySnapshot.documents
        return documents.mapNotNull { document ->
            val imageUrl = document.getString("imageUrl").toString()
            val itemName = document.getString("itemName").toString()
            val location = document.getString("location").toString()
            val date = document.getString("date").toString()
            val time = document.getString("time").toString()
            val itemDescription = document.getString("description").toString()
            val itemType = document.getString("itemType").toString()

            PostedItems(
                imageUrl,
                itemName,
                location,
                date,
                time,
                itemDescription,
                itemType,
            )
        }
    }

    private suspend fun fetchFoundItems(userId: String): List<PostedItems> {
        val querySnapshot = dbFoundItems.whereEqualTo("userId", userId).get().await()
        val documents = querySnapshot.documents
        return documents.mapNotNull { document ->
            val imageUrl = document.getString("imageUrl").toString()
            val itemName = document.getString("itemName").toString()
            val location = document.getString("location").toString()
            val date = document.getString("date").toString()
            val time = document.getString("time").toString()
            val itemDescription = document.getString("description").toString()
            val itemType = document.getString("itemType").toString()

            PostedItems(
                imageUrl,
                itemName,
                location,
                date,
                time,
                itemDescription,
                itemType,
            )
        }
    }
}
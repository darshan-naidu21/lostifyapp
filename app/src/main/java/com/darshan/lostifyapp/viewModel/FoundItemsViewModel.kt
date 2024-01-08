package com.darshan.lostifyapp.viewModel

import androidx.lifecycle.ViewModel
import com.darshan.lostifyapp.models.FoundItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FoundItemsViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dbFoundItems = db.collection("Found Items")

    private val _foundItems = MutableStateFlow<List<FoundItems>>(emptyList())
    val foundItems: StateFlow<List<FoundItems>> = _foundItems

    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    init {
        fetchFoundItems(uid.toString())
    }

    private fun fetchFoundItems(userId:String) {
        dbFoundItems.whereNotEqualTo("userId", userId).get()
            .addOnSuccessListener { snapshot ->
                val foundItemsList = snapshot.documents.mapNotNull { document ->
                    val imageUrl = document.getString("imageUrl").toString()
                    val itemName = document.getString("itemName").toString()
                    val location = document.getString("location").toString()
                    val date = document.getString("date").toString()
                    val time = document.getString("time").toString()
                    val itemDescription = document.getString("description").toString()
                    val itemCategory = document.getString("itemCategory").toString()
                    val username = document.getString("postedBy").toString()
                    val email = document.getString("email").toString()
                    val contactNumber = document.getString("contactNumber").toString()

                    FoundItems(
                        imageUrl,
                        itemName,
                        location,
                        date,
                        time,
                        itemDescription,
                        itemCategory,
                        username,
                        email,
                        contactNumber
                    )
                }
                _foundItems.value = foundItemsList
            }
            .addOnFailureListener { exception ->
                //
            }
    }

    private val _filteredFoundItems = MutableStateFlow<List<FoundItems>>(emptyList())
    val filteredFoundItems: StateFlow<List<FoundItems>> = _filteredFoundItems

    fun filterFoundItemsByCategory(category: String) {
        val filteredItems = if (category == "All Items") {
            _foundItems.value // Return all items
        } else {
            _foundItems.value.filter { it.itemCategory == category }
        }
        _filteredFoundItems.value = filteredItems
    }

    private val _searchFoundItems = MutableStateFlow<List<FoundItems>>(emptyList())
    val searchFoundItems: StateFlow<List<FoundItems>> = _searchFoundItems

    fun searchFoundItemsByName(query: String) {
        val filteredItems = _foundItems.value.filter { it.itemName.contains(query, ignoreCase = true) }
        _searchFoundItems.value = filteredItems
    }
}
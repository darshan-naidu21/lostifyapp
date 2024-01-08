package com.darshan.lostifyapp.viewModel

import androidx.lifecycle.ViewModel
import com.darshan.lostifyapp.models.LostItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LostItemsViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dbLostItems = db.collection("Lost Items")

    private val _lostItems = MutableStateFlow<List<LostItems>>(emptyList())
    val lostItems: StateFlow<List<LostItems>> = _lostItems

    private val uid = FirebaseAuth.getInstance().currentUser?.uid

    init {
        fetchLostItems(uid.toString())
    }

    private fun fetchLostItems(userId:String) {
        dbLostItems.whereNotEqualTo("userId", userId).get()
            .addOnSuccessListener { snapshot ->
                val lostItemsList = snapshot.documents.mapNotNull { document ->
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

                    LostItems(
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
                _lostItems.value = lostItemsList
            }
            .addOnFailureListener { exception ->
                // Handle failure case
            }
    }

    private val _filteredLostItems = MutableStateFlow<List<LostItems>>(emptyList())
    val filteredLostItems: StateFlow<List<LostItems>> = _filteredLostItems

    fun filterLostItemsByCategory(category: String) {
        val filteredItems = if (category == "All Items") {
            _lostItems.value // Return all items
        } else {
            _lostItems.value.filter { it.itemCategory == category }
        }
        _filteredLostItems.value = filteredItems
    }

    private val _searchLostItems = MutableStateFlow<List<LostItems>>(emptyList())
    val searchLostItems: StateFlow<List<LostItems>> = _searchLostItems

    fun searchLostItemsByName(query: String) {
        val filteredItems = _lostItems.value.filter { it.itemName.contains(query, ignoreCase = true) }
        _searchLostItems.value = filteredItems
    }
}
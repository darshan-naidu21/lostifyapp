package com.darshan.lostifyapp.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class DeleteItemViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dbLostItems = db.collection("Lost Items")
    private val dbFoundItems = db.collection("Found Items")

    fun deleteItem(imageUrl: String, itemType: String, context: Context) {
        val collection = if (itemType == "Lost") dbLostItems else dbFoundItems

        collection.whereEqualTo("itemType", itemType)
            .whereEqualTo("imageUrl", imageUrl)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                        .addOnSuccessListener(){
                            Toast.makeText(context, "Item removed from the listings.", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener(){
                            Toast.makeText(context, "An error occurred while removing the item.", Toast.LENGTH_SHORT).show()
                        }
                }

            }
            .addOnFailureListener { exception ->
                //Log.e(TAG, "Error deleting item: $exception")
            }
    }
}
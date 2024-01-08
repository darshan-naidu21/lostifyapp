package com.darshan.lostifyapp.viewModel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.darshan.lostifyapp.models.Items
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddItemViewModel : ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dbLostItems: CollectionReference = db.collection("Lost Items")
    private val dbFoundItems: CollectionReference = db.collection("Found Items")
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference.child("images")

    fun addItem(
        itemName: String,
        itemType: String,
        itemCategory: String,
        date: String,
        time: String,
        location: String,
        description: String,
        imageUri: Uri,
        context: Context
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        uid?.let { userId ->
            val userDocRef = db.collection("Lostify Users").document(userId)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username").toString()
                        val email = document.getString("email").toString()
                        val contactNo = document.getString("contactNumber").toString()

                        // Upload the image file to Firebase Storage
                        val imageRef = storageRef.child("$userId/${System.currentTimeMillis()}.jpg")
                        val uploadTask = imageRef.putFile(imageUri)

                        uploadTask.addOnSuccessListener {
                            // Image upload successful, get the download URL
                            val imageUrlTask = imageRef.downloadUrl

                            imageUrlTask.addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()

                                val itemDetails = Items(
                                    username,
                                    userId,
                                    email,
                                    contactNo,
                                    itemName,
                                    itemType,
                                    itemCategory,
                                    date,
                                    time,
                                    location,
                                    description,
                                    imageUrl
                                )

                                val itemCollection = if (itemType == "Lost") dbLostItems else dbFoundItems
                                val itemDocument = itemCollection.document()

                                itemDocument.set(itemDetails)
                                    .addOnSuccessListener {
                                        val message = if (itemType == "Lost") "The lost item has been added!"
                                        else "The found item has been added!"

                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        val message = if (itemType == "Lost") "Unable to add lost item!"
                                        else "Unable to add found item!"
                                        Toast.makeText(context, "$message\n$e", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                            .addOnFailureListener { e ->
                                //
                            }
                    } else {
                        //
                    }
                }
                .addOnFailureListener { exception ->
                    //
                }
        }
    }
}
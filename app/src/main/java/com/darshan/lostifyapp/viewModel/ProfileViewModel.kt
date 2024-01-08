package com.darshan.lostifyapp.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darshan.lostifyapp.models.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel: ViewModel() {

    val state = mutableStateOf(UserProfile())

    val uid = FirebaseAuth.getInstance().currentUser?.uid

    val db = FirebaseFirestore.getInstance()

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference.child("images")

    init {
        getData()
    }

    private fun getData(){
        viewModelScope.launch {
            state.value = getUserDataFromFireStore()

        }
    }

    private suspend fun getUserDataFromFireStore(): UserProfile {

        var data = UserProfile()

        try {
            val documentSnapshot = db.collection("Lostify Users").document(uid.toString()).get().await()
            if (documentSnapshot.exists()) {
                val result = documentSnapshot.toObject(UserProfile::class.java)
                result?.let { userProfile ->
                    data = userProfile
                }
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "getDataFromFireStore: $e")
        }

        return data
    }

    fun updateName(newName: String) {
        state.value = state.value.copy(name = newName)
    }

    fun updateEmail(newEmail: String) {
        state.value = state.value.copy(email = newEmail)
    }

    fun updateContactNumber(newContactNumber: String) {
        state.value = state.value.copy(contactNumber = newContactNumber)
    }

    fun updateUsername(newUsername: String) {
        state.value = state.value.copy(username = newUsername)
    }

    fun updatePassword(newPassword: String) {
        state.value = state.value.copy(password = newPassword)
    }

    fun updateUserDetails(context: Context, profilePicUri: Uri? = null) {
        uid?.let { uid ->
            // Update user details in Firestore
            db.collection("Lostify Users").document(uid).set(state.value)
                .addOnSuccessListener {
                    // Change email and password in Firebase Authentication
                    val user = FirebaseAuth.getInstance().currentUser

                    user?.let { currentUser ->
                        currentUser.updateEmail(state.value.email)
                            .addOnSuccessListener {
                                // Email updated successfully
                                currentUser.updatePassword(state.value.password)
                                    .addOnSuccessListener {
                                        // Password updated successfully
                                        if (profilePicUri != null) {
                                            // Upload the image file to Firebase Storage
                                            val imageRef = storageRef.child("$uid/${System.currentTimeMillis()}.jpg")
                                            val uploadTask = profilePicUri.let { uri ->
                                                imageRef.putFile(
                                                    uri
                                                )
                                            }

                                            uploadTask.addOnSuccessListener {
                                                val imageUrlTask = imageRef.downloadUrl

                                                imageUrlTask.addOnSuccessListener { uri ->
                                                    val profilePic = uri.toString()

                                                    val updatePic = hashMapOf<String, Any>("profilePic" to profilePic)

                                                    db.collection("Lostify Users").document(uid)
                                                        .update(updatePic)
                                                        .addOnSuccessListener {
                                                            updateLostItemDB()
                                                            updateFoundItemDB()
                                                            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                                        }
                                                        .addOnFailureListener {
                                                            Toast.makeText(context, "An error occurred while updating your profile picture!", Toast.LENGTH_SHORT).show()
                                                        }
                                                }
                                            }
                                        }
                                        else {
                                            updateLostItemDB()
                                            updateFoundItemDB()
                                            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "An error occurred while updating your password!", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "An error occurred while updating your email!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "An error occurred while updating your profile!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateLostItemDB() {
        val query = db.collection("Lost Items").whereEqualTo("userId", uid)

        query.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val updateData = hashMapOf<String, Any>(
                        "postedBy" to state.value.username,
                        "email" to state.value.email,
                        "contactNumber" to state.value.contactNumber
                    )

                    for (document in snapshot.documents) {
                        db.collection("Lost Items").document(document.id)
                            .update(updateData)
                            .addOnSuccessListener {
                                //Toast.makeText(context, "Lost and found items updated successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                //Toast.makeText(context, "An error occurred while updating lost and found items!", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    //Toast.makeText(context, "No matching lost and found items found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                //Toast.makeText(context, "An error occurred while querying lost and found items!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateFoundItemDB() {
        val query = db.collection("Found Items").whereEqualTo("userId", uid)

        query.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    val updateData = hashMapOf<String, Any>(
                        "postedBy" to state.value.username,
                        "email" to state.value.email,
                        "contactNumber" to state.value.contactNumber
                    )

                    for (document in snapshot.documents) {
                        db.collection("Found Items").document(document.id)
                            .update(updateData)
                            .addOnSuccessListener {
                                //Toast.makeText(context, "Lost and found items updated successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                //Toast.makeText(context, "An error occurred while updating lost and found items!", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    //Toast.makeText(context, "No matching lost and found items found!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                //Toast.makeText(context, "An error occurred while querying lost and found items!", Toast.LENGTH_SHORT).show()
            }
    }
}

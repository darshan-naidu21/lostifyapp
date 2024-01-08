package com.darshan.lostifyapp.viewModel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.darshan.lostifyapp.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationViewModel() : ViewModel() {

    fun createAccount(
        name: String,
        email: String,
        contactNo: String,
        username: String,
        password: String,
        context: android.content.Context
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid;

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val dbUsers: CollectionReference = db.collection("Lostify Users")

        val profilePic = "https://firebasestorage.googleapis.com/v0/b/lostifyapp-9d595.appspot.com/o/images%2FdefaultProfilePic.png?alt=media&token=34271e19-c526-4c12-92a8-f7907b296189&_gl=1*16ydza7*_ga*MTE1NzQwNjcxNC4xNjgwMTU3Mjk5*_ga_CW55HF8NVT*MTY4NjMzNTQzMC41MC4xLjE2ODYzNDQwODkuMC4wLjA."

        val userDetails = Users(name,email,contactNo,username,password, uid.toString(), profilePic)

        if (uid != null) {
            dbUsers.document(uid).set(userDetails).addOnSuccessListener {

                Toast.makeText(
                    context,
                    "Your registration is successful!",
                    Toast.LENGTH_SHORT
                ).show()

            }.addOnFailureListener { e ->
                Toast.makeText(context, "Unable to add data! \n$e", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
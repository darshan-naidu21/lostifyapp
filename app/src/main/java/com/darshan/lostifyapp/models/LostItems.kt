package com.darshan.lostifyapp.models

data class LostItems(
    val imageUrl: String,
    val itemName: String,
    val location: String,
    val date: String,
    val time: String,
    val itemDescription: String,
    val itemCategory: String,
    val username: String,
    val email: String,
    val contactNumber: String
)

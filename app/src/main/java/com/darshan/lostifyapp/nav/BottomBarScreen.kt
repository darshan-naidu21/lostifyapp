package com.darshan.lostifyapp.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ViewList
import com.darshan.lostifyapp.R

sealed class BottomBarScreen(
    val id: String,
    val icon: Int
){
    object Home: BottomBarScreen("Home", R.drawable.home)
    object AddItem: BottomBarScreen("AddItem", R.drawable.add)
    object View: BottomBarScreen("View", R.drawable.view)
    object Profile: BottomBarScreen("Profile", R.drawable.profile)

    object Items{
        val list = listOf(
            Home, AddItem, View, Profile
        )
    }
}

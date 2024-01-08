package com.darshan.lostifyapp.nav

sealed class Routes(val route: String) {
    object Login: Routes("Login")
    object Register: Routes("Register")
    object Home: Routes("Home")
    object AddItem: Routes("AddItem")
    object View: Routes("View")
    object Profile: Routes("Profile")
    object LostItemDetails: Routes("LostItemDetails")
    object FoundItemDetails: Routes("FoundItemDetails")
}

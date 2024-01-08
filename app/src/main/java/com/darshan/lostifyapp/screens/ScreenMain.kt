package com.darshan.lostifyapp.screens

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.darshan.lostifyapp.nav.Routes
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenMain(
    auth: FirebaseAuth
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(
            route = Routes.Login.route
        ) {
            Login(navController = navController)
        }
        composable(
            route = Routes.Register.route
        ) {
            Register(navController = navController, auth = auth)
        }
        composable(
            route = Routes.Home.route
        ) {
            Home(navController = navController)
        }
        composable(
            route = Routes.AddItem.route
        ) {
            AddItem(navController = navController)
        }
        composable(
            route = Routes.View.route
        ) {
            PostedItems(navController = navController)
        }
        composable(
            route = Routes.Profile.route
        ) {
            Profile(navController = navController, viewModel())
        }
        composable(
            route = Routes.LostItemDetails.route +
                    "/{encodedImageUrl}" +
                    "/{itemName}" +
                    "/{location}" +
                    "/{date}" +
                    "/{time}" +
                    "/{itemDescription}" +
                    "/{username}" +
                    "/{email}" +
                    "/{contactNumber}",

            arguments = listOf(
                navArgument("encodedImageUrl") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("itemName") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("location") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("date") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("time") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("itemDescription") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("username") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("contactNumber") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                })
        ) { backStackEntry ->
            val encodedImageUrl = backStackEntry.arguments?.getString("encodedImageUrl").toString()
            val itemName = backStackEntry.arguments?.getString("itemName").toString()
            val location = backStackEntry.arguments?.getString("location").toString()
            val date = backStackEntry.arguments?.getString("date").toString()
            val time = backStackEntry.arguments?.getString("time").toString()
            val itemDescription = backStackEntry.arguments?.getString("itemDescription").toString()
            val username = backStackEntry.arguments?.getString("username").toString()
            val email = backStackEntry.arguments?.getString("email").toString()
            val contactNumber = backStackEntry.arguments?.getString("contactNumber").toString()

            val decodedImageUrl = Uri.decode(encodedImageUrl)

            val imageUrl: String = decodedImageUrl.replace("images/", "images%2F").replace("Q2/", "Q2%2F");

            LostItemDetails(
                navController = navController,
                imageUrl,
                itemName,
                location,
                date,
                time,
                itemDescription,
                username,
                email,
                contactNumber
            )
        }
        composable(
            route = Routes.FoundItemDetails.route +
                    "/{encodedImageUrl}" +
                    "/{itemName}" +
                    "/{location}" +
                    "/{date}" +
                    "/{time}" +
                    "/{itemDescription}" +
                    "/{username}" +
                    "/{email}" +
                    "/{contactNumber}",

            arguments = listOf(
                navArgument("encodedImageUrl") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("itemName") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("location") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("date") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("time") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("itemDescription") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("username") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("contactNumber") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                })
        ) { backStackEntry ->
            val encodedImageUrl = backStackEntry.arguments?.getString("encodedImageUrl").toString()
            val itemName = backStackEntry.arguments?.getString("itemName").toString()
            val location = backStackEntry.arguments?.getString("location").toString()
            val date = backStackEntry.arguments?.getString("date").toString()
            val time = backStackEntry.arguments?.getString("time").toString()
            val itemDescription = backStackEntry.arguments?.getString("itemDescription").toString()
            val username = backStackEntry.arguments?.getString("username").toString()
            val email = backStackEntry.arguments?.getString("email").toString()
            val contactNumber = backStackEntry.arguments?.getString("contactNumber").toString()

            val decodedImageUrl = Uri.decode(encodedImageUrl)

            val imageUrl: String = decodedImageUrl.replace("images/", "images%2F").replace("Q2/", "Q2%2F");

            FoundItemDetails(
                navController = navController,
                imageUrl,
                itemName,
                location,
                date,
                time,
                itemDescription,
                username,
                email,
                contactNumber
            )
        }
    }
}

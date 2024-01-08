package com.darshan.lostifyapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.darshan.lostifyapp.screens.ScreenMain
import com.darshan.lostifyapp.ui.theme.LostifyAppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private val auth by lazy{
        Firebase.auth
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            LostifyAppTheme {
                ScreenMain(auth = auth)
            }
        }
    }
}

@Preview(showBackground = true)

@Composable
fun DefaultPreview() {
    LostifyAppTheme {
    }
}
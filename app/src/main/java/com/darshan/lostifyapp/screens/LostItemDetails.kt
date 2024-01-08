package com.darshan.lostifyapp.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
//import coil.compose.AsyncImage
import com.darshan.lostifyapp.nav.Routes
import com.darshan.lostifyapp.ui.theme.Poppins

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LostItemDetails(
    navController: NavController,
    imageUrl: String,
    itemName: String,
    location: String,
    date: String,
    time: String,
    itemDescription: String,
    username: String,
    email: String,
    contactNumber: String,
) {
    val context = LocalContext.current

    val callPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactNumber"))
            context.startActivity(callIntent)
        } else {
            //
        }
    }

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$contactNumber")
                putExtra("sms_body", "Message body")
            }
            context.startActivity(smsIntent)
        } else {
            //
        }
    }

    Scaffold(
        backgroundColor = Color(0xFF16132E),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier.height(18.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(
                    onClick = {
                        navController.navigate(
                            route = Routes.Home.route
                        ) {
                            popUpTo(
                                Routes.Home.route
                            ) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp)),
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF070020)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "",
                            tint = Color.Gray
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .size(220.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                ) {
//                    AsyncImage(
//                        model = imageUrl,
//                        contentDescription = "Lost Item Image",
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = itemName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFFF4ECFF)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Location",
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color(0xFFEC457A)
                )

                Text(text = location, fontSize = 14.sp, fontFamily = Poppins, color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Date",
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color(0xFFEC457A)
                )

                Text(text = date, fontSize = 14.sp, fontFamily = Poppins, color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Time",
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color(0xFFEC457A)
                )

                Text(text = time, fontSize = 14.sp, fontFamily = Poppins, color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Description",
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = Color(0xFFEC457A)
                )

                Text(text = itemDescription, fontSize = 14.sp, fontFamily = Poppins, color = Color.White)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Found the item? Help reunite it with its owner:",
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color(0xFFDAA520)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val smsPermission = android.Manifest.permission.SEND_SMS
                            if (ContextCompat.checkSelfPermission(context, smsPermission) == PackageManager.PERMISSION_GRANTED) {
                                val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("smsto:$contactNumber")
                                    putExtra("sms_body", "Message body")
                                }
                                context.startActivity(smsIntent)
                            }
                            else {
                                smsPermissionLauncher.launch(smsPermission)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFEC457A)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Chat,
                            contentDescription = "Message",
                            tint = Color.White
                        )
                        Text(
                            text = "Message",
                            modifier = Modifier.padding(start = 6.dp),
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            val permission = android.Manifest.permission.CALL_PHONE
                            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactNumber"))
                                context.startActivity(callIntent)
                            } else {
                                callPermissionLauncher.launch(permission)
                            }
                        },
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFEC457A)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Phone,
                            contentDescription = "Call",
                            tint = Color.White
                        )
                        Text(
                            text = "Call",
                            modifier = Modifier.padding(start = 6.dp),
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                            color = Color.White
                        )
                    }
                }

            }
        }
    }
}
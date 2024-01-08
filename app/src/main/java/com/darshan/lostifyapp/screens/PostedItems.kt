package com.darshan.lostifyapp.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.darshan.lostifyapp.models.PostedItems
import com.darshan.lostifyapp.nav.BottomBarScreen
import com.darshan.lostifyapp.nav.Routes
import com.darshan.lostifyapp.ui.theme.Poppins
import com.darshan.lostifyapp.viewModel.DeleteItemViewModel
import com.darshan.lostifyapp.viewModel.PostedItemsViewModel
import com.darshan.lostifyapp.widgets.CustomBottomNavigation

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun PostedItems(navController: NavController){
    val currentScreen = mutableStateOf<BottomBarScreen>(BottomBarScreen.View)

//    val currentScreen by rememberSaveable { mutableStateOf(BottomBarScreen.View) }

    Scaffold(
        bottomBar =  {
            CustomBottomNavigation(currentScreenId = currentScreen.value.id) {
                currentScreen.value = it
                when(currentScreen.value) {
                    BottomBarScreen.Home -> navController.navigate(Routes.Home.route) {
                        popUpTo("Home") {
                            inclusive = true
                        }
                    }
                    BottomBarScreen.AddItem -> navController.navigate(Routes.AddItem.route) {
                        popUpTo("AddItem") {
                            inclusive = true
                        }
                    }
                    BottomBarScreen.Profile -> navController.navigate(Routes.Profile.route) {
                        popUpTo("Profile") {
                            inclusive = true
                        }
                    }
                    else -> {}
                }
            }
        },
        backgroundColor = Color(0xFF16132e)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding()) // Add padding for bottom navigation bar
                .verticalScroll(rememberScrollState())
        ) {
            PostedItemsSection(viewModel())
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PostedItemsSection(viewModel: PostedItemsViewModel) {
    Column() {
        Spacer(Modifier.height(20.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Posted Items", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold, color = Color(0xFFEC457A), fontFamily = Poppins)
        }
        Spacer(Modifier.height(10.dp))

        val userItems by viewModel.userItems.collectAsState()

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            if (userItems.isEmpty()) {
                Text(
                    text = "You haven't posted any lost or found items yet.",
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray
                )
            } else {
                for (item in userItems) {
                    PostedItemCard(item)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun PostedItemCard(postedItem: PostedItems, viewModel: DeleteItemViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        backgroundColor = Color(0xFF9B94B8),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(70.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = postedItem.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .weight(1.5f)
                ) {
                    Text(
                        text = postedItem.itemName,
                        color = Color(0xFF16132E),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location Icon",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = postedItem.location,
                            fontSize = 12.sp,
                            fontFamily = Poppins,
                            color = Color.DarkGray
                        )
                    }
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = if (expanded) {
                            "Show less"
                        } else {
                            "Show more"
                        }
                    )
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Description",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF09080C)
                )

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = postedItem.itemDescription,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color(0xFF353437)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Date",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF09080C)
                )

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = postedItem.date,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color(0xFF353437)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Time",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF09080C)
                )

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = postedItem.time,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color(0xFF353437)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Type",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color(0xFF09080C)
                )

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = postedItem.itemType,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color(0xFF353437)
                )

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = {
                        viewModel.deleteItem(postedItem.imageUrl, postedItem.itemType, context)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEC457A)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckBox,
                        contentDescription = "Mark as Complete",
                        tint = Color.White
                    )
                    Text(
                        text = "Mark as Complete",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 5.dp),
                        color = Color(0xFFFFFFFF)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

package com.darshan.lostifyapp.widgets

import android.media.Image
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.darshan.lostifyapp.nav.BottomBarScreen

@Composable
fun CustomBottomNavigation(
    currentScreenId: String,
    onItemSelected: (BottomBarScreen) -> Unit
){
    val items = BottomBarScreen.Items.list

    Row(
        modifier = Modifier
            .background(Color(0xFF1F1A40))
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        items.forEach {item ->
            CustomBottomNavigationItem(
                item = item,
                isSelected = item.id == currentScreenId,
            ){
                onItemSelected(item)
            }
        }
    }
}

@Composable
fun CustomBottomNavigationItem(
    navController: NavController = rememberNavController(),
    item: BottomBarScreen,
    isSelected: Boolean,
    onClick: () -> Unit
){
    val selectedColor = Color(0xFFEC457A)
    val unselectedColor = Color.Gray

    Box(
        modifier = Modifier
//            .clip(CircleShape)
//            .background(if (isSelected) Color(0xFF16132E) else Color(0xFF1F1A40))
            .clickable(onClick = { onClick() })
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(item.icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(if (isSelected) selectedColor else unselectedColor)
        )
//        Row(
//            modifier = Modifier
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ){
//            Image(
//                painter = painterResource(item.icon),
//                contentDescription = null,
////                modifier = Modifier.size(48.dp),
//                colorFilter = ColorFilter.tint(if (isSelected) selectedColor else unselectedColor)
//            )
//            Icon(
//                imageVector = item,
//                contentDescription = null,
//                tint = if (isSelected) selectedColor else unselectedColor
//            )
//
//            AnimatedVisibility(visible = isSelected) {
//                AnimatedVisibility(visible = isSelected) {
//                    Text(
//                        text = item.title,
//                        color = selectedColor
//                    )
//                }
//
//                AnimatedVisibility(visible = !isSelected) {
//                    Text(
//                        text = item.title,
//                        color = unselectedColor
//                    )
//                }
//            }
        }
    }

package com.darshan.lostifyapp.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.darshan.lostifyapp.models.FoundItems
import com.darshan.lostifyapp.models.LostItems
import com.darshan.lostifyapp.nav.BottomBarScreen
import com.darshan.lostifyapp.widgets.CustomBottomNavigation
import com.darshan.lostifyapp.nav.Routes
import com.darshan.lostifyapp.ui.theme.Billabong
import com.darshan.lostifyapp.ui.theme.Poppins
import com.darshan.lostifyapp.viewModel.FoundItemsViewModel
import com.darshan.lostifyapp.viewModel.LostItemsViewModel
import com.darshan.lostifyapp.viewModel.ProfileViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun Home(
    navController: NavController = rememberNavController(),
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))

    val currentScreen = mutableStateOf<BottomBarScreen>(BottomBarScreen.Home)

    val profileViewModel: ProfileViewModel = viewModel()

    val getUserData = profileViewModel.state.value

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .height(56.dp)
                    .padding(top = 12.dp),
                elevation = 4.dp,
                backgroundColor = Color(0xFF16132e),
                title = {
                    Text(
                        text = "Lostify",
                        fontFamily = Billabong,
                        fontSize = 32.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 3.dp)
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 3.dp)
                    ) {
                        IconButton(onClick = {
                            navController.navigate(
                                route = Routes.Profile.route
                            ) {
                                popUpTo(
                                    Routes.Profile.route
                                ) {
                                    inclusive = true
                                }
                            }
                        }) {
                            AsyncImage(
                                model = getUserData.profilePic,
                                contentDescription = "Profile picture",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }
                        IconButton({
                            navController.navigate(
                                route = Routes.Login.route
                            ) {
                                popUpTo(
                                    Routes.Home.route
                                ) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            CustomBottomNavigation(currentScreenId = currentScreen.value.id) {
                currentScreen.value = it
                when (currentScreen.value) {
                    BottomBarScreen.AddItem -> navController.navigate(Routes.AddItem.route) {
                        popUpTo("AddItem") {
                            inclusive = true
                        }
                    }
                    BottomBarScreen.View -> navController.navigate(Routes.View.route) {
                        popUpTo("View") {
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
        ){
            // Define the list of categories
            val categories = listOf(
                "All Items",
                "Electronics",
                "Wallets & Purses",
                "Clothing & Accessories",
                "Bags",
                "Identification Documents",
                "Watches",
                "Jewelry",
                "Other"
            )

            // Currently selected category
            var selectedCategory by remember { mutableStateOf(categories.first()) }

            Column(
                Modifier.fillMaxSize()
            ) {
                SearchBar(searchQuery, { searchQuery = it })

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categories) { category ->
                        Category(
                            category = category,
                            isSelected = category == selectedCategory,
                            onCategorySelected = { selectedCategory = category }
                        )
                    }
                }

                if (searchQuery.isNotBlank()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    SearchLostItemsSection(viewModel(), searchQuery, navController)
                    Spacer(modifier = Modifier.height(20.dp))
                    SearchFoundItemsSection(viewModel(), searchQuery, navController)
                    Spacer(modifier = Modifier.height(32.dp))
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                    LostItemsSection(viewModel(), navController, selectedCategory)
                    Spacer(modifier = Modifier.height(20.dp))
                    FoundItemsSection(viewModel(), navController, selectedCategory)
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .height(53.dp)
            .width(550.dp),
        value = searchQuery,
        onValueChange = {onSearchQueryChange(it)},
        placeholder = {
            Text(
                text = "Search for lost or found items",
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = Poppins
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "",
                tint = Color(0xFF373073)
            )
        },
        trailingIcon = {
            if(searchQuery.isNotBlank())
                IconButton(
                    onClick = {
                        onSearchQueryChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "",
                        tint = Color(0xFF373073)

                    )
                }
        },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color(0xFF373073),
            backgroundColor = Color(0xFF1f1a36),
            textColor = Color.White,
            placeholderColor = Color.Gray,
        )
    )
}

@Composable
fun SearchLostItemsSection(
    viewModel: LostItemsViewModel,
    searchQuery: String,
    navController: NavController
) {
    Column(){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Lost Items - Search Results", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold, color = Color(0xFFEC457A), fontFamily = Poppins)
        }
        Spacer(Modifier.height(4.dp))

        viewModel.searchLostItemsByName(searchQuery)

        val lostItems by viewModel.searchLostItems.collectAsState()

        if (lostItems.isEmpty()) {
            Text(
                text = "No results found.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(lostItems) { lostItem ->
                    LostItemCard(lostItem = lostItem, navController)
                }
            }
        }
    }
}

@Composable
fun SearchFoundItemsSection(
    viewModel: FoundItemsViewModel,
    searchQuery: String,
    navController: NavController
) {
    Column(){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Items Found - Search Results", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold, color = Color(0xFFEC457A), fontFamily = Poppins)
        }
        Spacer(Modifier.height(4.dp))

        viewModel.searchFoundItemsByName(searchQuery)

        val foundItems by viewModel.searchFoundItems.collectAsState()

        if (foundItems.isEmpty()) {
            Text(
                text = "No results found.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                for (foundItem in foundItems) {
                    FoundItemCard(foundItem = foundItem, navController)
                }
            }
        }
    }
}

@Composable
fun Category(
    category: String,
    isSelected: Boolean,
    onCategorySelected: (String) -> Unit
) {
    TextButton(
        onClick = { onCategorySelected(category) },
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) Color(0xFFEC457A) else Color.LightGray)
            .padding(horizontal = 20.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (isSelected) Color.White else Color.Black
        )
    ) {
        Text(text = category)
    }
}

@Composable
fun LostItemsSection(
    viewModel: LostItemsViewModel,
    navController: NavController,
    selectedCategory: String
) {
    Column(){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Lost Items", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold, color = Color(0xFFEC457A), fontFamily = Poppins)
        }
        Spacer(Modifier.height(4.dp))

        val lostItems by viewModel.lostItems.collectAsState()

        viewModel.filterLostItemsByCategory(selectedCategory)

        val filteredLostItems by viewModel.filteredLostItems.collectAsState()

        if (lostItems.isEmpty()) {
            Text(
                text = "No lost items have been reported yet.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
        else if (filteredLostItems.isEmpty()) {
            Text(
                text = "No items found in this category.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
        else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredLostItems) { lostItem ->
                    // Filter lost items based on the selected category
                    if (lostItem.itemCategory == selectedCategory) {
                        LostItemCard(lostItem = lostItem, navController)
                    }
                    else if (selectedCategory == "All Items") {
                        LostItemCard(lostItem = lostItem, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun LostItemCard(lostItem: LostItems, navController: NavController) {
    val encodedImageUrl = Uri.encode(lostItem.imageUrl)
    Card(
        Modifier
            .width(160.dp)
            .height(260.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate(
                    route = Routes.LostItemDetails.route +
                            "/" + encodedImageUrl +
                            "/" + lostItem.itemName +
                            "/" + lostItem.location +
                            "/" + lostItem.date +
                            "/" + lostItem.time +
                            "/" + lostItem.itemDescription +
                            "/" + lostItem.username +
                            "/" + lostItem.email +
                            "/" + lostItem.contactNumber
                ) {
                    popUpTo(
                        Routes.LostItemDetails.route +
                                "/" + encodedImageUrl +
                                "/" + lostItem.itemName +
                                "/" + lostItem.location +
                                "/" + lostItem.date +
                                "/" + lostItem.time +
                                "/" + lostItem.itemDescription +
                                "/" + lostItem.username +
                                "/" + lostItem.email +
                                "/" + lostItem.contactNumber
                    ) {
                        inclusive = true
                    }
                }
            },
        backgroundColor = Color(0xFF9B94B8)//#FBF7FF //#AEAA6CB //9B94B8 //CEC6EC
    ) {
        Column(
            Modifier
                .padding(bottom = 8.dp)
        ) {
            AsyncImage(
                model = lostItem.imageUrl,
                contentDescription = "Lost Item Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(8.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = lostItem.itemName,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    color = Color(0xFF16132e)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Icon",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = lostItem.location,
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Color.DarkGray
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(text = "Posted by:", fontFamily = Poppins, fontSize = 10.sp, color = Color.DarkGray)
                Text(text = lostItem.username, fontWeight = FontWeight.Bold, fontFamily = Poppins, fontSize = 12.sp, color = Color.Black)
            }
        }
    }
}

@Composable
fun FoundItemsSection(
    viewModel: FoundItemsViewModel,
    navController: NavController,
    selectedCategory: String
) {
    Column(){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Items Found", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold, color = Color(0xFFEC457A), fontFamily = Poppins)
        }
        Spacer(Modifier.height(4.dp))

        val foundItems by viewModel.foundItems.collectAsState()

        viewModel.filterFoundItemsByCategory(selectedCategory)

        val filteredFoundItems by viewModel.filteredFoundItems.collectAsState()

        if (foundItems.isEmpty()) {
            Text(
                text = "No found items have been reported yet.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
        else if (filteredFoundItems.isEmpty()) {
            Text(
                text = "No items found in this category.",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                for (foundItem in filteredFoundItems) {
                    if (foundItem.itemCategory == selectedCategory) {
                        FoundItemCard(foundItem = foundItem, navController)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    else if (selectedCategory == "All Items") {
                        FoundItemCard(foundItem = foundItem, navController)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FoundItemCard(foundItem: FoundItems, navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF9B94B8)),
        contentAlignment = Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(50.dp),
                contentAlignment = Center
            ) {
                AsyncImage(
                    model = foundItem.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(start = 15.dp),
            ) {
                Text(
                    text = foundItem.itemName,
                    color = Color(0xFF16132E),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Icon",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = foundItem.location,
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    val encodedImageUrl = Uri.encode(foundItem.imageUrl)
                    navController.navigate(
                        route = Routes.FoundItemDetails.route +
                                "/" + encodedImageUrl +
                                "/" + foundItem.itemName +
                                "/" + foundItem.location +
                                "/" + foundItem.date +
                                "/" + foundItem.time +
                                "/" + foundItem.itemDescription +
                                "/" + foundItem.username +
                                "/" + foundItem.email +
                                "/" + foundItem.contactNumber
                    ) {
                        popUpTo(
                            route = Routes.FoundItemDetails.route +
                                    "/" + encodedImageUrl +
                                    "/" + foundItem.itemName +
                                    "/" + foundItem.location +
                                    "/" + foundItem.date +
                                    "/" + foundItem.time +
                                    "/" + foundItem.itemDescription +
                                    "/" + foundItem.username +
                                    "/" + foundItem.email +
                                    "/" + foundItem.contactNumber
                        ) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color(0xFF16132E),
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}
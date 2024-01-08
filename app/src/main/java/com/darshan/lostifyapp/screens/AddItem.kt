package com.darshan.lostifyapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.darshan.lostifyapp.nav.BottomBarScreen
import com.darshan.lostifyapp.nav.Routes
import com.darshan.lostifyapp.ui.theme.Poppins
import com.darshan.lostifyapp.viewModel.AddItemViewModel
import com.darshan.lostifyapp.widgets.CustomBottomNavigation
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddItem(
    navController: NavController,
    context: Context = LocalContext.current,
    addItemViewModel: AddItemViewModel = viewModel()
) {
    val currentScreen = mutableStateOf<BottomBarScreen>(BottomBarScreen.AddItem)

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            singlePhotoPicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    var itemName by remember {
        mutableStateOf("")
    }

    var showItemError by remember {
        mutableStateOf(false)
    }

    var isItemTypeExpanded by remember {
        mutableStateOf(false)
    }
    var itemType by remember {
        mutableStateOf("")
    }

    var isCategoryExpanded by remember {
        mutableStateOf(false)
    }
    var itemCategory by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf("")
    }

    val calendarState = rememberSheetState()

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    if (showDatePicker) {
        CalendarDialog(
            state = calendarState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH
                //disabledDates = //listOf(LocalDate.now().minusDays(7))
            ),
            selection = CalendarSelection.Date(
                onNegativeClick = ({showDatePicker = false}),
                selectedDate = LocalDate.now()
            ) { selectedDate ->
                date = selectedDate.toString()
                showDatePicker = false
            },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        )
        calendarState.show()
    }

    var time by remember {
        mutableStateOf("")
    }

    val clockState = rememberSheetState()

    val focusRequester = remember { FocusRequester() }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    if (showTimePicker) {
        ClockDialog(
            state = clockState,
            config = ClockConfig(
                is24HourFormat = false
            ),
            selection = ClockSelection.HoursMinutes(
                onNegativeClick = ({showTimePicker = false})
            ) { hours, minutes ->
                val localTime = LocalTime.of(hours, minutes)
                val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)
                time = localTime.format(formatter)
                showTimePicker = false
            }
        )
        clockState.show()
    }

    var location by remember {
        mutableStateOf("")
    }
    var showLocationError by remember {
        mutableStateOf(false)
    }

    var description by remember {
        mutableStateOf("")
    }
    var showDescriptionError by remember {
        mutableStateOf(false)
    }

    var showBlankInputError by remember {
        mutableStateOf(false)
    }

    var showImageError by remember {
        mutableStateOf(false)
    }

    Scaffold (
        bottomBar =  {
            CustomBottomNavigation(currentScreenId = currentScreen.value.id) {
                currentScreen.value = it
                when(currentScreen.value) {
                    BottomBarScreen.Home -> navController.navigate(Routes.Home.route) {
                        popUpTo("Home") {
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
                .verticalScroll(rememberScrollState()) // Use verticalScroll modifier
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Add Lost/Found Item", style = MaterialTheme.typography.h6, color = Color(0xFFEC457A), fontFamily = Poppins)
                }
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (uri != null) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "No Image",
                            modifier = Modifier.size(120.dp),
                            tint = Color.Gray
                        )
                    }
                }
                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED) {
                            singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Choose Image")
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(62.dp),
                    value = itemName,
                    onValueChange = {itemName = it},
                    label = {
                        Text(
                            text = "Item Name",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = Poppins
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Poppins
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Label,
                            contentDescription = "",
                            tint = Color(0xFF373073)
                        )
                    },
                    trailingIcon = {
                        if(itemName.isNotBlank())
                            IconButton(
                                onClick = {
                                    itemName = ""
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
                        cursorColor = Color(0xFF373073),
                        textColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = isItemTypeExpanded,
                    onExpandedChange = {isItemTypeExpanded = it}
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(62.dp),
                        value = itemType,
                        onValueChange = {},
                        label = {
                            Text(
                                text = "Item Type",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontFamily = Poppins
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = Poppins
                        ),
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "",
                                tint = Color(0xFF373073)
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isItemTypeExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            cursorColor = Color(0xFF373073),
                            textColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = isItemTypeExpanded,
                        onDismissRequest = { isItemTypeExpanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                itemType = "Lost"
                                isItemTypeExpanded = false
                            }
                        ){
                            Text(text = "Lost")
                        }
                        DropdownMenuItem(
                            onClick = {
                                itemType = "Found"
                                isItemTypeExpanded = false
                            }
                        ){
                            Text(text = "Found")
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = isCategoryExpanded,
                    onExpandedChange = {isCategoryExpanded = it}
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(62.dp),
                        value = itemCategory,
                        onValueChange = {},
                        label = {
                            Text(
                                text = "Item Category",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontFamily = Poppins
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = Poppins
                        ),
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = "",
                                tint = Color(0xFF373073)
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            cursorColor = Color(0xFF373073),
                            textColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = isCategoryExpanded,
                        onDismissRequest = { isCategoryExpanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                itemCategory = "Electronics"
                                isCategoryExpanded = false
                            }
                        ){
                            Text(text = "Electronics")
                        }
                        DropdownMenuItem(
                            onClick = {
                                itemCategory = "Wallets & Purses"
                                isCategoryExpanded = false
                            }
                        ){
                            Text(text = "Wallets & Purses")
                        }
                        DropdownMenuItem(
                            onClick = {
                                itemCategory = "Clothing & Accessories"
                                isCategoryExpanded = false
                            }
                        ){
                            Text(text = "Clothing & Accessories")
                        }
                        DropdownMenuItem(
                            onClick = {
                                itemCategory = "Bags"
                                isCategoryExpanded = false
                            }
                        ){
                            Text(text = "Bags")
                        }
                        DropdownMenuItem(
                            onClick = {
                                itemCategory = "Identification Documents"
                                isCategoryExpanded = false
                            }
                        ){
                            Text(text = "Identification Documents")
                        }
                        DropdownMenuItem(
                            onClick = {
                                itemCategory = "Watches"
                                isCategoryExpanded = false
                            }
                        ){
                            Text(text = "Watches")
                        }
                        DropdownMenuItem(
                            onClick = {
                                itemCategory = "Jewelry"
                                isCategoryExpanded = false
                            }
                        ){
                            Text(text = "Jewelry")
                        }
                        DropdownMenuItem(
                            onClick = {
                                itemCategory = "Other"
                                isCategoryExpanded = false
                            }
                        ){
                            Text(text = "Other")
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(62.dp),
                    value = date,
                    onValueChange = {date = it},
                    label = {
                        Text(
                            text = "Date",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = Poppins
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Poppins
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "",
                            tint = Color(0xFF373073)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                showDatePicker = true
                            }

                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Event,
                                contentDescription = "",
                                tint = Color(0xFF373073)
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color(0xFF373073),
                        textColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(62.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                //focusManager.clearFocus()
                                showTimePicker = true
                            }
                        },
                    value = time,
                    onValueChange = {time = it},
                    label = {
                        Text(
                            text = "Time",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = Poppins
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Poppins
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = "",
                            tint = Color(0xFF373073)
                        )
                    },
                    trailingIcon = {
                        if(time.isNotBlank())
                            IconButton(
                                onClick = {
                                    time = ""
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
                        cursorColor = Color(0xFF373073),
                        textColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(62.dp),
                    value = location,
                    onValueChange = {location = it},
                    label = {
                        Text(
                            text = "Location",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = Poppins
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Poppins
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "",
                            tint = Color(0xFF373073)
                        )
                    },
                    trailingIcon = {
                        if(location.isNotBlank())
                            IconButton(
                                onClick = {
                                    location = ""
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
                        cursorColor = Color(0xFF373073),
                        textColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    value = description,
                    onValueChange = {description = it},
                    label = {
                        Text(
                            text = "Item Description",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = Poppins
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = Poppins
                    ),
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Description,
                            contentDescription = "",
                            tint = Color(0xFF373073)
                        )
                    },
                    trailingIcon = {
                        if(description.isNotBlank())
                            IconButton(
                                onClick = {
                                    description = ""
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
                        cursorColor = Color(0xFF373073),
                        textColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )

                Spacer(
                    modifier = Modifier.height(25.dp)
                )

                // Define minimum and maximum length
                val minLength = 3
                val maxLength = 15

                val minDescLength = 10
                val maxDescLength = 50

                Button(
                    onClick = {
                        if (itemName.isBlank() || itemType.isBlank() || itemCategory.isBlank() || date.isBlank() || time.isBlank() || location.isBlank() || description.isBlank()) {
                            showBlankInputError = true
                        }
                        else if (itemName.length < minLength || itemName.length > maxLength) {
                            showItemError = true
                        }
                        else if (location.length < minLength || location.length > maxLength) {
                            showLocationError = true
                        }
                        else if (description.length < minDescLength || description.length > maxDescLength) {
                            showDescriptionError = true
                        }
                        else if (uri == null) {
                            showImageError = true
                        }
                        else {
                            uri?.let { imageUri ->
                                addItemViewModel.addItem(
                                    itemName,
                                    itemType,
                                    itemCategory,
                                    date,
                                    time,
                                    location,
                                    description,
                                    imageUri,
                                    context
                                )
                            }
                        }
                    },
                    enabled = true,
                    modifier = Modifier.width(300.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEC457A)
                    )
                ) {
                    Text(
                        text = "Upload Item",
                        color = Color(0xFFFFFFFF)
                    )
                }
                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                if (showBlankInputError) {
                    AlertDialog(
                        onDismissRequest = { showBlankInputError = false },
                        title = { Text(text = "Blank field") },
                        text = { Text(text = "You are required to fill up all the fields.") },
                        confirmButton = {
                            TextButton(
                                onClick = { showBlankInputError = false }
                            ) {
                                Text(text = "OK")
                            }
                        },
                        backgroundColor = Color(0xFFFFD8E4),
                        contentColor = Color(0xFF523e62)
                    )
                }

                if (showImageError) {
                    AlertDialog(
                        onDismissRequest = { showImageError = false },
                        title = { Text(text = "No image chosen") },
                        text = { Text(text = "Please choose an image.") },
                        confirmButton = {
                            TextButton(
                                onClick = { showImageError = false }
                            ) {
                                Text(text = "OK")
                            }
                        },
                        backgroundColor = Color(0xFFFFD8E4),
                        contentColor = Color(0xFF523e62)
                    )
                }

                if (showItemError) {
                    AlertDialog(
                        onDismissRequest = { showItemError = false },
                        title = { Text(text = "Invalid item name") },
                        text = { Text(text = "Item name must be between $minLength and $maxLength characters.") },
                        confirmButton = {
                            TextButton(
                                onClick = { showItemError = false }
                            ) {
                                Text(text = "OK")
                            }
                        },
                        backgroundColor = Color(0xFFFFD8E4),
                        contentColor = Color(0xFF523e62)
                    )
                }

                if (showLocationError) {
                    AlertDialog(
                        onDismissRequest = { showLocationError = false },
                        title = { Text(text = "Invalid location") },
                        text = { Text(text = "Location must be between $minLength and $maxLength characters.") },
                        confirmButton = {
                            TextButton(
                                onClick = { showLocationError = false }
                            ) {
                                Text(text = "OK")
                            }
                        },
                        backgroundColor = Color(0xFFFFD8E4),
                        contentColor = Color(0xFF523e62)
                    )
                }

                if (showDescriptionError) {
                    AlertDialog(
                        onDismissRequest = { showDescriptionError = false },
                        title = { Text(text = "Invalid item description") },
                        text = { Text(text = "Item description must be between $minDescLength and $maxDescLength characters.") },
                        confirmButton = {
                            TextButton(
                                onClick = { showDescriptionError = false }
                            ) {
                                Text(text = "OK")
                            }
                        },
                        backgroundColor = Color(0xFFFFD8E4),
                        contentColor = Color(0xFF523e62)
                    )
                }
            }
        }
    }
}
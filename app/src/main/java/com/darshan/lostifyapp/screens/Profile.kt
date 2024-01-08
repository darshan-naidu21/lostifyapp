package com.darshan.lostifyapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.darshan.lostifyapp.nav.BottomBarScreen
import com.darshan.lostifyapp.nav.Routes
import com.darshan.lostifyapp.ui.theme.Poppins
import com.darshan.lostifyapp.viewModel.ProfileViewModel
import com.darshan.lostifyapp.viewModel.UserExistenceViewModel
import com.darshan.lostifyapp.widgets.CustomBottomNavigation

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun Profile(navController: NavController, viewModel: ProfileViewModel) {
    var profilePicUri by remember { mutableStateOf<Uri?>(null) }

    val getUserData = viewModel.state.value

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    val currentScreen = mutableStateOf<BottomBarScreen>(BottomBarScreen.Profile)

    val context = LocalContext.current

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            profilePicUri = it
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

    var showNameError by remember {
        mutableStateOf(false)
    }
    var showEmailError by remember {
        mutableStateOf(false)
    }
    var showPhoneError by remember {
        mutableStateOf(false)
    }
    var showUsernameError by remember {
        mutableStateOf(false)
    }
    var showPasswordError by remember {
        mutableStateOf(false)
    }
    var showBlankInputError by remember {
        mutableStateOf(false)
    }

    val userExistenceViewModel = viewModel<UserExistenceViewModel>()

    var isUsernameAvailable by remember { mutableStateOf(true) }

    var isEmailAvailable by remember { mutableStateOf(true) }

    var isContactNoAvailable by remember { mutableStateOf(true) }

    var showUsernameTaken by remember {
        mutableStateOf(false)
    }
    var showEmailTaken by remember {
        mutableStateOf(false)
    }
    var showContactNoTaken by remember {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(currentScreenId = currentScreen.value.id) {
                currentScreen.value = it
                when (currentScreen.value) {
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
                    BottomBarScreen.View -> navController.navigate(Routes.View.route) {
                        popUpTo("View") {
                            inclusive = true
                        }
                    }
                    else -> {}
                }
            }
        },
        backgroundColor = Color(0xFF16132e)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "My Profile", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold, color = Color(0xFFEC457A), fontFamily = Poppins)
            }
            Spacer(Modifier.height(10.dp))

            Box(){
                AsyncImage(
                    model = if (profilePicUri != null) profilePicUri else getUserData.profilePic,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(50.dp))
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(BorderStroke(3.dp, color = Color(0xFFEC457A)))
                        .background(Color(0xFFEC457A))
                        .size(28.dp)
                        .align(alignment = Alignment.BottomEnd)
                        .clickable{
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED) {
                                singlePhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                ){
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Add profile picture",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(62.dp),
                value = getUserData.name,
                onValueChange = {
                    viewModel.updateName(it)
                },
                label = {
                    Text(
                        text = "Name",
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
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountBox,
                        contentDescription = "",
                        tint = Color(0xFF373073)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color(0xFF373073),
                    textColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(62.dp),
                value = getUserData.email,
                onValueChange = {
                    viewModel.updateEmail(it)
                },
                label = {
                    Text(
                        text = "Email",
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
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "",
                        tint = Color(0xFF373073)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color(0xFF373073),
                    textColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(62.dp),
                value = getUserData.contactNumber,
                onValueChange = {
                    viewModel.updateContactNumber(it)
                },
                label = {
                    Text(
                        text = "Contact Number",
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
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = "",
                        tint = Color(0xFF373073)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color(0xFF373073),
                    textColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(62.dp),
                value = getUserData.username,
                onValueChange = {
                    viewModel.updateUsername(it)
                },
                label = {
                    Text(
                        text = "Username",
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
                        imageVector = Icons.Filled.Person,
                        contentDescription = "",
                        tint = Color(0xFF373073)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color(0xFF373073),
                    textColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(62.dp),
                value = getUserData.password,
                onValueChange = {
                    viewModel.updatePassword(it)
                },
                label = {
                    Text(
                        text = "Password",
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
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "",
                        tint = Color(0xFF373073)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        Icon(
                            imageVector = if(isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Password Toggle",
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

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    if (getUserData.name.isBlank() || getUserData.email.isBlank() || getUserData.contactNumber.isBlank() || getUserData.username.isBlank() || getUserData.password.isBlank()) {
                        showBlankInputError = true
                    }
                    else {
                        if (!isNameValid(getUserData.name)) {
                            showNameError = true
                        }
                        else if (!isEmailValid(getUserData.email)) {
                            showPasswordError = true
                        }
                        else if (!isPhoneValid(getUserData.contactNumber)) {
                            showPhoneError = true
                        }
                        else if (!isUsernameValid(getUserData.username)) {
                            showUsernameError = true
                        }
                        else if (!isPasswordValid(getUserData.password)) {
                            showPasswordError = true
                        }
                        else {
                            if (!isEmailAvailable) {
                                showEmailTaken = true
                            }
                            else if (!isContactNoAvailable) {
                                showContactNoTaken = true
                            }
                            else if (!isUsernameAvailable) {
                                showUsernameTaken = true
                            }
                            else {
                                viewModel.updateUserDetails(context, profilePicUri)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFEC457A)
                )
            ) {
                Text(
                    text = "Update Profile",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = Poppins
                )
            }

            userExistenceViewModel.isUsernameAvailable(getUserData.username) { available ->
                isUsernameAvailable = available
            }

            userExistenceViewModel.isEmailAvailable(getUserData.email) { available ->
                isEmailAvailable = available
            }

            userExistenceViewModel.isContactNoAvailable(getUserData.contactNumber) { available ->
                isContactNoAvailable = available
            }

            if (showNameError) {
                AlertDialog(
                    onDismissRequest = { showNameError = false },
                    title = { Text(text = "Invalid Name") },
                    text = { Text(text = "Please enter a valid name using only letters and spaces, starting with an uppercase letter, such as \"John Doe\" or \"John\".") },
                    confirmButton = {
                        TextButton(
                            onClick = { showNameError = false }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    backgroundColor = Color(0xFFFFD8E4),
                    contentColor = Color(0xFF523e62)
                )
            }

            if (showEmailError) {
                AlertDialog(
                    onDismissRequest = { showEmailError = false },
                    title = { Text(text = "Invalid Email") },
                    text = { Text(text = "Please enter a valid email address.") },
                    confirmButton = {
                        TextButton(
                            onClick = { showEmailError = false }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    backgroundColor = Color(0xFFFFD8E4),
                    contentColor = Color(0xFF523e62)
                )
            }

            if (showPhoneError) {
                AlertDialog(
                    onDismissRequest = { showPhoneError = false },
                    title = { Text(text = "Invalid Contact Number") },
                    text = { Text(text = "Please enter a valid contact number using only digits.") },
                    confirmButton = {
                        TextButton(
                            onClick = { showPhoneError = false }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    backgroundColor = Color(0xFFFFD8E4),
                    contentColor = Color(0xFF523e62)
                )
            }

            if (showUsernameError) {
                AlertDialog(
                    onDismissRequest = { showUsernameError = false },
                    title = { Text(text = "Invalid Username") },
                    text = { Text(text = "Please enter a valid username using only lowercase letters, digits, and underscores.") },
                    confirmButton = {
                        TextButton(
                            onClick = { showUsernameError = false }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    backgroundColor = Color(0xFFFFD8E4),
                    contentColor = Color(0xFF523e62)
                )
            }

            if (showPasswordError) {
                AlertDialog(
                    onDismissRequest = { showPasswordError = false },
                    title = { Text(text = "Invalid Password") },
                    text = { Text(text = "Password must be at least 8 characters long, include a mix of uppercase and lowercase letters and numbers only.") },
                    confirmButton = {
                        TextButton(
                            onClick = { showPasswordError = false }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    backgroundColor = Color(0xFFFFD8E4),
                    contentColor = Color(0xFF523e62)
                )
            }

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

            if (showUsernameTaken) {
                AlertDialog(
                    onDismissRequest = { showUsernameTaken = false },
                    title = { Text(text = "Username Taken") },
                    text = { Text(text = "The username you entered is already associated with an existing user.") },
                    confirmButton = {
                        TextButton(
                            onClick = { showUsernameTaken = false }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    backgroundColor = Color(0xFFFFD8E4),
                    contentColor = Color(0xFF523e62)
                )
            }

            if (showEmailTaken) {
                AlertDialog(
                    onDismissRequest = { showEmailTaken = false },
                    title = { Text(text = "Email Taken") },
                    text = { Text(text = "The email address you entered is already associated with an existing user.") },
                    confirmButton = {
                        TextButton(
                            onClick = { showEmailTaken = false }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    backgroundColor = Color(0xFFFFD8E4),
                    contentColor = Color(0xFF523e62)
                )
            }

            if (showContactNoTaken) {
                AlertDialog(
                    onDismissRequest = { showContactNoTaken = false },
                    title = { Text(text = "Contact Number Taken") },
                    text = { Text(text = "The contact number you entered is already associated with an existing user.") },
                    confirmButton = {
                        TextButton(
                            onClick = { showContactNoTaken = false }
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

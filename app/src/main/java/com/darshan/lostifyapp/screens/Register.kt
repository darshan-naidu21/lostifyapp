package com.darshan.lostifyapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.darshan.lostifyapp.nav.Routes
import com.darshan.lostifyapp.ui.theme.Poppins
import com.darshan.lostifyapp.viewModel.UsernameViewModel
import com.darshan.lostifyapp.viewModel.ContactNoViewModel
import com.darshan.lostifyapp.viewModel.EmailViewModel
import com.darshan.lostifyapp.viewModel.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun Register(
    navController: NavController = rememberNavController(),
    auth: FirebaseAuth,
    registrationViewModel: RegistrationViewModel = viewModel()
) {
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var contactNo by remember {
        mutableStateOf("")
    }
    var username by remember {

        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val usernameViewModel = viewModel<UsernameViewModel>()

    var isUsernameAvailable by remember { mutableStateOf(true) }

    val emailViewModel = viewModel<EmailViewModel>()

    var isEmailAvailable by remember { mutableStateOf(true) }

    val contactNoViewModel = viewModel<ContactNoViewModel>()

    var isContactNoAvailable by remember { mutableStateOf(true) }

    var showAccountFailed by remember {
        mutableStateOf(false)
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
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
    var showUsernameTaken by remember {
        mutableStateOf(false)
    }
    var showEmailTaken by remember {
        mutableStateOf(false)
    }
    var showContactNoTaken by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Column(
            Modifier.fillMaxSize()
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
                            route = Routes.Login.route
                        ) {
                            popUpTo(
                                Routes.Login.route
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
                            .background(Color(0xFF16132e)),
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
                modifier = Modifier.height(12.dp)
            )
            Card(
                Modifier
                    .weight(4f)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(32.dp),
                backgroundColor = Color(0xFF16132e)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(28.dp)
                ) {
                    Text(
                        text = "Create Account.",
                        color = Color(0xFFccccff),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    )
                    Text(
                        text = "Welcome to Lostify! Please provide your details below to get started with the app.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(62.dp),
                            value = name,
                            onValueChange = {name = it},
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
                                imeAction = ImeAction.Next
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.AccountBox,
                                    contentDescription = "",
                                    tint = Color(0xFF373073)
                                )
                            },
                            trailingIcon = {
                                if(name.isNotBlank())
                                    IconButton(
                                        onClick = {
                                            name = ""
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
                                unfocusedIndicatorColor = Color(0xFF373073),
                                textColor = Color.White
                            )
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(62.dp),
                            value = email,
                            onValueChange = {email = it},
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
                                imeAction = ImeAction.Next
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = "",
                                    tint = Color(0xFF373073)
                                )
                            },
                            trailingIcon = {
                                if(email.isNotBlank())
                                    IconButton(
                                        onClick = {
                                            email = ""
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
                                unfocusedIndicatorColor = Color(0xFF373073),
                                textColor = Color.White
                            )
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(62.dp),
                            value = contactNo,
                            onValueChange = {contactNo = it},
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
                            trailingIcon = {
                                if(contactNo.isNotBlank())
                                    IconButton(
                                        onClick = {
                                            contactNo = ""
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
                                unfocusedIndicatorColor = Color(0xFF373073),
                                textColor = Color.White
                            )
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(62.dp),
                            value = username,
                            onValueChange = {username = it},
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
                            trailingIcon = {
                                if(username.isNotBlank())
                                    IconButton(
                                        onClick = {
                                            username = ""
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
                                unfocusedIndicatorColor = Color(0xFF373073),
                                textColor = Color.White
                            )
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(62.dp),
                            value = password,
                            onValueChange = {password = it},
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
                                unfocusedIndicatorColor = Color(0xFF373073),
                                textColor = Color.White
                            )
                        )
                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )
                        Button(
                            onClick = {
                                if (name.isBlank() || email.isBlank() || contactNo.isBlank() || username.isBlank() || password.isBlank()) {
                                    showBlankInputError = true
                                }
                                else {
                                    if (!isNameValid(name)) {
                                        showNameError = true
                                    }
                                    else if (!isEmailValid(email)) {
                                        showPasswordError = true
                                    }
                                    else if (!isPhoneValid(contactNo)) {
                                        showPhoneError = true
                                    }
                                    else if (!isUsernameValid(username)) {
                                        showUsernameError = true
                                    }
                                    else if (!isPasswordValid(password)) {
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
                                            auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        registrationViewModel.createAccount(
                                                            name,
                                                            email,
                                                            contactNo,
                                                            username,
                                                            password,
                                                            context
                                                        )
                                                        navController.navigate(Routes.Login.route)

                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "An error occurred while creating your account!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        }
                                    }
                                }
                            },
                            enabled = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFEC457A)
                            )
                        ) {
                            Text(
                                text = "Sign Up",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = Poppins
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Already a member?"
                            )
                            TextButton(
                                onClick = {
                                    navController.navigate(
                                        route = Routes.Login.route
                                    ) {
                                        popUpTo(
                                            Routes.Login.route
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = "Log In",
                                    color = Color(0xFFEC457A)
                                )
                            }
                        }

                        usernameViewModel.isUsernameAvailable(username) { available ->
                            isUsernameAvailable = available
                        }

                        emailViewModel.isEmailAvailable(email) { available ->
                            isEmailAvailable = available
                        }

                        contactNoViewModel.isContactNoAvailable(contactNo) { available ->
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

                        if (showAccountFailed) {
                            AlertDialog(
                                onDismissRequest = { showAccountFailed = false },
                                title = { Text(text = "Account Creation Failed") },
                                text = { Text(text = "We apologize, but there was an error during the account creation process.") },
                                confirmButton = {
                                    TextButton(
                                        onClick = { showAccountFailed = false }
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
        }
    }
}

fun isNameValid(name: String): Boolean {
    val regex = "\\b[A-Z][a-z]*([ ]?[A-Z][a-z]*)*\\b".toRegex()
    return regex.matches(name)
}

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPhoneValid(contactNo: String): Boolean {
    return contactNo.isDigitsOnly()
}

fun isUsernameValid(username: String): Boolean {
    val minLength = 6
    val maxLength = 20
    val allowedChars = "abcdefghijklmnopqrstuvwxyz0123456789_"

    if (username.length < minLength || username.length > maxLength) {
        return false
    }

    for (char in username) {
        if (char !in allowedChars) {
            return false
        }
    }

    return true
}

fun isPasswordValid(password: String): Boolean {
    val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$".toRegex()
    return regex.matches(password)
}
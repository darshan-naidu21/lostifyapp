package com.darshan.lostifyapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.darshan.lostifyapp.R
import com.darshan.lostifyapp.nav.Routes
import com.darshan.lostifyapp.ui.theme.Poppins
import com.darshan.lostifyapp.viewModel.LoginViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun Login(
    navController: NavController = rememberNavController(),
    loginViewModel: LoginViewModel = viewModel()
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    var showLoginFailed by remember {
        mutableStateOf(false)
    }

    var showEmailError by remember {
        mutableStateOf(false)
    }

    var showPasswordError by remember {
        mutableStateOf(false)
    }

    var showBlankInputError by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painterResource(R.drawable.anxiety_amico),
                contentDescription = "App Logo",
                modifier = Modifier
                    .weight(1f)
                    .size(180.dp),
            )
            Card(
                Modifier
                    .weight(2.5f)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(32.dp),
                backgroundColor = Color(0xFF16132e)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Text(
                        text = "Log In to Lostify.",
                        color = Color(0xFFccccff),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    )
                    Text(
                        text = "Lost and found made easy with Lostify. Please enter your login credentials below.",
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
                            value = loginViewModel.email,
                            onValueChange = {loginViewModel.email = it},
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
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "",
                                    tint = Color(0xFF373073)
                                )
                            },
                            trailingIcon = {
                                if(loginViewModel.email.isNotBlank())
                                    IconButton(
                                        onClick = {
                                            loginViewModel.email = ""
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
                                textColor = Color.White,
                            )
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(62.dp),
                            value = loginViewModel.password,
                            onValueChange = {loginViewModel.password = it},
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
                                textColor = Color.White,
                            )
                        )
                        Spacer(
                            modifier = Modifier.height(30.dp)
                        )
                        Button(
                            onClick = {
                                if (loginViewModel.email.isBlank() || loginViewModel.password.isBlank()) {
                                    showBlankInputError = true
                                }
                                else {
                                    if (!isEmailValid(loginViewModel.email)) {
                                        showEmailError = true
                                    }
                                    else if (!isPasswordValid(loginViewModel.password)) {
                                        showPasswordError = true
                                    }
                                    else {
                                        scope.launch {
                                            val data = loginViewModel.logInWithEmail()

                                            if(data!= null) {
                                                navController.navigate(
                                                    route = Routes.Home.route
                                                ) {
                                                    popUpTo(
                                                        Routes.Login.route
                                                    ) {
                                                        inclusive = true
                                                    }
                                                }
                                            } else {
                                                showLoginFailed = true
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
                                text = "Log In",
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
                                text = "Not a member yet?"
                            )
                            TextButton(
                                onClick = {
                                    navController.navigate(
                                        route = Routes.Register.route
                                    )
                                }
                            ) {
                                Text(
                                    text = "Sign Up",
                                    color = Color(0xFFEC457A)
                                )
                            }
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

                        if (showPasswordError) {
                            AlertDialog(
                                onDismissRequest = { showPasswordError = false },
                                title = { Text(text = "Invalid Password") },
                                text = { Text(text = "Password must be at least 8 characters long, include a mix of uppercase and lowercase letters and numbers.") },
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
                                text = { Text(text = "You are required to fill up both the fields.") },
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

                        if (showLoginFailed) {
                            AlertDialog(
                                onDismissRequest = { showLoginFailed = false },
                                title = { Text(text = "Login Failed") },
                                text = { Text(text = "Incorrect email or password. Please try again.") },
                                confirmButton = {
                                    TextButton(
                                        onClick = { showLoginFailed = false }
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


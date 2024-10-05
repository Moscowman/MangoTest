package com.vadimraspopin.mangotest.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vadimraspopin.mangotest.R
import com.vadimraspopin.mangotest.repository.FakeAuthRepository
import com.vadimraspopin.mangotest.viewmodel.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavHostController,
    phoneNumber: String,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val registerState by viewModel.registerState.collectAsState()

    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.registration_screen_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }) { paddingValues ->
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val offsetY = screenHeight * 0.2f

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize()
                .offset(y = offsetY),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.registration_screen_phone_number_label) + ": $phoneNumber"
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.name.value,
                onValueChange = { viewModel.name.value = it },
                label = { Text(stringResource(R.string.registration_screen_name_textfield_suggestion)) },
                modifier = Modifier.widthIn(max = 488.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.username.value,
                onValueChange = {
                    val regex = Regex("[A-Za-z0-9\\-_]")
                    val filteredInput = it.filter { regex.matches(it.toString()) }
                    viewModel.username.value = filteredInput
                },
                label = { Text(stringResource(R.string.registration_screen_username_textfield_suggestion)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii
                ),
                modifier = Modifier.widthIn(max = 488.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (registerState is ApiUiRequestState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    enabled = viewModel.name.value.isNotEmpty()
                            && viewModel.username.value.isNotEmpty(),
                    onClick = {
                        viewModel.register(
                            phoneNumber
                        )
                    },
                    modifier = Modifier.widthIn(max = 320.dp),
                ) {
                    Text(stringResource(R.string.registration_screen_register_button_label))
                }
            }
        }
    }

    val localizedErrorMessage = when (registerState) {
        is ApiUiRequestState.Error -> {
            localizeErrorMessage((registerState as ApiUiRequestState.Error).message)
        }

        else -> null
    }

    LaunchedEffect(localizedErrorMessage) {
        localizedErrorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(registerState) {
        when (registerState) {
            is ApiUiRequestState.Success -> {
                //TODO
            }

            else -> Unit
        }
    }
}

@Composable
private fun localizeErrorMessage(errorMessage: String): String =
    when (errorMessage) {
        "User with this username already exists" -> stringResource(R.string.registration_screen_username_already_exists_error_message)
        "User with this phone already exists" -> stringResource(R.string.registration_screen_phone_already_exists_error_message)
        else -> errorMessage
    }


@Preview
@Composable
fun RegistrationScreenPreview() {

    val fakeAuthRepository = FakeAuthRepository()

    val navController = rememberNavController()

    val registrationViewModel = RegistrationViewModel(fakeAuthRepository)

    RegistrationScreen(navController, "+7 925 123-45-67", registrationViewModel)
}
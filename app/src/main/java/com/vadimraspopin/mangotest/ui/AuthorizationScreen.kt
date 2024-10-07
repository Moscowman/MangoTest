package com.vadimraspopin.mangotest.ui

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.vadimraspopin.mangotest.R
import com.vadimraspopin.mangotest.repository.FakeAuthRepository
import com.vadimraspopin.mangotest.viewmodel.AuthViewModel
import java.lang.Character.isDigit

const val CODE_MAX_LENGTH = 6

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorizationScreen(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {

    val sendAuthCodeState by authViewModel.sendAuthCodeState.collectAsState()
    val checkAuthCodeState by authViewModel.checkAuthCodeState.collectAsState()

    val code = authViewModel.code
    val phoneNumber = authViewModel.phoneNumber

    val context = LocalContext.current
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val countryCode = tm.networkCountryIso
    val countryCodePickerState = rememberKomposeCountryCodePickerState(
        defaultCountryCode = countryCode,
    )
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.authorization_screen_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
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

            OutlinedTextField(
                value = phoneNumber.value,

                leadingIcon = {
                    KomposeCountryCodePicker(
                        modifier = Modifier,
                        showOnlyCountryCodePicker = true,
                        text = phoneNumber.value,
                        state = countryCodePickerState,
                        onValueChange = {}
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                onValueChange = {
                    phoneNumber.value =
                        it.filter { char -> char.isDigit() || char == '-' || char == ' ' }
                },
                label = { Text(stringResource(R.string.authorization_screen_phone_edit_label)) },
                placeholder = { Text("XXX XXX-XX-XX") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.widthIn(max = 488.dp)
            )

            if (sendAuthCodeState is ApiUiRequestState.Error) {
                val localizedMessages = (sendAuthCodeState as ApiUiRequestState.Error)
                    .messages.map { localizeCheckAuthCodeErrorMessage(it) }

                val displayMessage = localizedMessages.joinToString(separator = "\n")
                Text(
                    text = displayMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    authViewModel.fullPhoneNumber = countryCodePickerState.getFullPhoneNumber()
                    authViewModel.sendAuthCode()
                },
                enabled = phoneNumber.value.filter { it -> isDigit(it) }.isNotEmpty(),
                modifier = Modifier.widthIn(max = 320.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(stringResource(R.string.authorization_screen_get_confirmation_code_button_label))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (sendAuthCodeState is ApiUiRequestState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            if (sendAuthCodeState is ApiUiRequestState.Success) {

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = code.value.toString(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    onValueChange = { newText ->
                        val digitsOnly = newText.filter { it.isDigit() }
                        if (digitsOnly.length <= CODE_MAX_LENGTH) {
                            code.value = digitsOnly
                        }
                    },
                    label = { Text(stringResource(R.string.authorization_screen_confirmation_code_textfield_suggestion)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.widthIn(max = 488.dp)
                )

                if (checkAuthCodeState is ApiUiRequestState.Error) {
                    val localizedMessages = (checkAuthCodeState as ApiUiRequestState.Error)
                        .messages.map { localizeCheckAuthCodeErrorMessage(it) }
                    val displayMessage = localizedMessages.joinToString(separator = "\n")
                    Text(
                        text = localizeCheckAuthCodeErrorMessage(displayMessage),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        authViewModel.code = code
                        authViewModel.checkAuthCode()
                    },
                    enabled = code.value.length == CODE_MAX_LENGTH,
                    modifier = Modifier.widthIn(max = 320.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(stringResource(R.string.authorization_screen_enter_system_button_label))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (checkAuthCodeState is ApiUiRequestState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            LaunchedEffect(checkAuthCodeState) {
                if (checkAuthCodeState is ApiUiRequestState.Success) {
                    authViewModel.fullPhoneNumber = countryCodePickerState.getFullPhoneNumber()
                    if ((checkAuthCodeState as ApiUiRequestState.Success).data.isUserExists) {
                        navController.navigate(Routes.CHATS) {
                            popUpTo(Routes.AUTHORIZATION) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate("registration/${authViewModel.fullPhoneNumber}")
                    }
                    authViewModel.resetAuthCode()
                }
            }
        }
    }
}

@Composable
fun localizeCheckAuthCodeErrorMessage(message: String): String {
    return if (message == "Not valid auth code") {
        stringResource(R.string.authorization_screen_check_auth_code_not_valid_error_message)
    } else {
        message
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorizationScreenPreview() {
    val fakeAuthRepository = FakeAuthRepository()

    val authViewModel = AuthViewModel(fakeAuthRepository)

    val navController = rememberNavController()

    AuthorizationScreen(navController, authViewModel)
}

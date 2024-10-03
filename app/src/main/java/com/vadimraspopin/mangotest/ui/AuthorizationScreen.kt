package com.vadimraspopin.mangotest.ui

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.vadimraspopin.mangotest.AuthViewModel
import com.vadimraspopin.mangotest.R
import com.vadimraspopin.mangotest.model.CheckAuthCodeResponse
import com.vadimraspopin.mangotest.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Character.isDigit

const val CODE_MAX_LENGTH = 6

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorizationScreen(authViewModel: AuthViewModel) {

    val code = authViewModel.code
    val phoneNumber = authViewModel.phoneNumber

    val context = LocalContext.current
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val countryCode = tm.networkCountryIso
    val countryCodePickerState = rememberKomposeCountryCodePickerState(
        defaultCountryCode = countryCode,
    )

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Вход в систему") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
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
                label = { Text(stringResource(R.string.phone_edit_label)) },
                placeholder = { Text("XXX XXX-XX-XX") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val fullPhoneNumber = countryCodePickerState.getFullPhoneNumber()
                    authViewModel.fullPhoneNumber = fullPhoneNumber
                    authViewModel.sendAuthCode()
                },
                enabled = phoneNumber.value.filter { it -> isDigit(it) }.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(stringResource(R.string.get_confirmation_code_button_label))
            }

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
                label = { Text(stringResource(R.string.confirmation_code_textfield_suggestion)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {},
                enabled = code.value.length == CODE_MAX_LENGTH,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(stringResource(R.string.enter_system_button_label))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorizationScreenPreview() {
    val fakeAuthRepository = object : AuthRepository {
        override fun sendAuthCode(phone: String): Flow<Unit> {
            return TODO()
        }

        override fun checkAuthCode(phone: String, code: String): Flow<CheckAuthCodeResponse> {
            return flow {
                emit(
                    CheckAuthCodeResponse(
                        refreshToken = "",
                        accessToken = "",
                        userId = 0,
                        isUserExists = false
                    )
                )
            }
        }
    }

    val authViewModel = AuthViewModel(fakeAuthRepository)

    AuthorizationScreen(authViewModel)
}

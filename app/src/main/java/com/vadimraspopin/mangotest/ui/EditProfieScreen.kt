package com.vadimraspopin.mangotest.ui

import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vadimraspopin.mangotest.R
import com.vadimraspopin.mangotest.di.BASE_API_URL
import com.vadimraspopin.mangotest.model.User
import com.vadimraspopin.mangotest.viewmodel.EditProfileViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(viewModel: EditProfileViewModel = hiltViewModel()) {
    val currentUser by viewModel.currentUser.collectAsState()
    val updateProfileState by viewModel.updateProfileState.collectAsState()

    var city by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var aboutMe by remember { mutableStateOf("") }
    var avatarUri: Uri? by remember { mutableStateOf(null) }

    val context = LocalContext.current

    val calendar = remember { Calendar.getInstance() }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    var showDatePicker by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            avatarUri = it
        }
    }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            city = user.city ?: ""
            birthday = user.birthday ?: ""
            aboutMe = user.status ?: ""
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                birthday = dateFormat.format(calendar.time)
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.edit_profile_screen_title)) })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        galleryLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (avatarUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUri),
                        contentDescription = stringResource(
                            R.string.edit_profile_screen_avatar_content_description
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else if (currentUser?.avatars?.avatar != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(
                                buildFullImageUrl(
                                    BASE_API_URL, currentUser?.avatars?.avatar ?: ""
                                )
                            )
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(
                            R.string.edit_profile_screen_avatar_content_description
                        ),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = stringResource(
                            R.string.edit_profile_screen_select_avatar_content_description
                        ),
                        tint = Color.Gray,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text(stringResource(R.string.edit_profile_screen_city_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = birthday,
                onValueChange = { /* Не обрабатываем изменения вручную */ },
                label = { Text(stringResource(R.string.edit_profile_screen_birthday_label)) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = stringResource(
                                R.string.edit_profile_screen_select_birthday_content_description
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = aboutMe,
                onValueChange = { aboutMe = it },
                label = { Text(stringResource(R.string.edit_profile_screen_about_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.updateProfile(
                        name = currentUser?.name ?: "",
                        username = currentUser?.username ?: "",
                        city = city,
                        birthday = birthday,
                        aboutMe = aboutMe,
                        avatarUri = avatarUri,
                        context
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.edit_profile_screen_save_button_label))
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = updateProfileState) {
                is ApiUiRequestState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is ApiUiRequestState.Success<User> -> {
                    Text(
                        stringResource(R.string.edit_profile_profile_updated),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is ApiUiRequestState.Error -> {
                    val localizedMessages =
                        state.messages.map { localizeUpdateProfileErrorMessage(it) }

                    val displayMessage = localizedMessages.joinToString(separator = "\n")
                    Text(
                        text = displayMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
fun localizeUpdateProfileErrorMessage(message: String): String {
    return if (message == "Not valid auth code") {
        stringResource(R.string.authorization_screen_check_auth_code_not_valid_error_message)
    } else {
        message
    }
}

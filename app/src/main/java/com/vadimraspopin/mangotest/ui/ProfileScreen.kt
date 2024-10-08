package com.vadimraspopin.mangotest.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vadimraspopin.mangotest.di.BASE_API_URL
import com.vadimraspopin.mangotest.getZodiacSign
import com.vadimraspopin.mangotest.model.User
import com.vadimraspopin.mangotest.R
import com.vadimraspopin.mangotest.viewmodel.ProfileState
import com.vadimraspopin.mangotest.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: ProfileViewModel = hiltViewModel()) {
    val profileState by viewModel.profileState.collectAsState()

    when (profileState) {
        is ProfileState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ProfileState.Success -> {
            val user = (profileState as ProfileState.Success).user
            ProfileContent(navController, user)
        }

        is ProfileState.Error -> {
            val message = (profileState as ProfileState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Ошибка: $message")
            }
        }
    }
}

@Composable
fun ProfileContent(navController: NavHostController, user: User, modifier: Modifier = Modifier) {
    val zodiacSign =
        remember(user.birthday) { user.birthday?.let { getZodiacSign(user.birthday) } ?: "" }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(buildFullImageUrl(BASE_API_URL, user.avatars?.avatar ?: ""))
                .crossfade(true)
                .build(),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(24.dp))

        InfoRow(label = stringResource(R.string.profile_screen_phone_label), value = user.phone ?: "")

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(label = stringResource(R.string.profile_screen_username_label), value = user.username)

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(label = stringResource(R.string.profile_screen_city_label), value = user.city ?: "")

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(
            label = stringResource(
                R.string.profile_screen_birthday_label
            ), value = user.birthday ?: ""
        )

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(label = stringResource(R.string.profile_screen_zodiak_label), value = zodiacSign)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.profile_screen_about_label),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = user.status ?: "",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(Routes.EDIT_PROFILE)
            },
            modifier = Modifier.widthIn(max = 320.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text("Редактировать профиль")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


fun buildFullImageUrl(baseUrl: String, relativePath: String): String {
    return if (baseUrl.endsWith("/") && relativePath.startsWith("/")) {
        baseUrl + relativePath.removePrefix("/")
    } else if (!baseUrl.endsWith("/") && !relativePath.startsWith("/")) {
        "$baseUrl/$relativePath"
    } else {
        baseUrl + relativePath
    }
}
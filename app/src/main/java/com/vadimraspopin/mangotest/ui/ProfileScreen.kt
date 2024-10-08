package com.vadimraspopin.mangotest.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vadimraspopin.mangotest.R
import com.vadimraspopin.mangotest.getZodiacSign
import com.vadimraspopin.mangotest.model.User
import com.vadimraspopin.mangotest.viewmodel.ProfileState
import com.vadimraspopin.mangotest.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val profileState by viewModel.profileState.collectAsState()

    when (profileState) {
        is ProfileState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ProfileState.Success -> {
            val user = (profileState as ProfileState.Success).user
            ProfileContent(user)
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
fun ProfileContent(user: User) {
    val zodiacSign = remember(user.birthday) { user.birthday?.let {getZodiacSign(user.birthday)} ?: "" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.placeholder_avatar),
            contentDescription = "Аватар пользователя",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        InfoRow(label = "Телефон", value = user.phone ?: "")

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(label = "Никнейм", value = user.username)

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(label = "Город", value = user.city ?: "")

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(label = "Дата рождения", value = user.birthday ?: "")

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow(label = "Знак зодиака", value = zodiacSign)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "О себе",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "About me placeholder",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
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
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
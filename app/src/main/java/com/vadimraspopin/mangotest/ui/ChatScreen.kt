package com.vadimraspopin.mangotest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vadimraspopin.mangotest.model.Message
import com.vadimraspopin.mangotest.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel = ChatViewModel()) {
    val messages = viewModel.messages
    var messageText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Чат") })
        },
        bottomBar = {
            MessageInput(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClicked = {
                    messageText = ""
                }
            )
        }
    ) { padding ->
        MessagesList(
            messages = messages,
            modifier = Modifier.padding(padding).fillMaxSize()
        )
    }
}

@Composable
fun MessagesList(messages: List<Message>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(messages) { message ->
            MessageItem(message)
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .background(
                    color = if (message.isMine) MaterialTheme.colorScheme.primary else Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            color = if (message.isMine) Color.White else Color.Black
        )
    }
}

@Composable
fun MessageInput(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Введите сообщение") },
            maxLines = 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
        IconButton(onClick = onSendClicked) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Отправить")
        }
    }
}
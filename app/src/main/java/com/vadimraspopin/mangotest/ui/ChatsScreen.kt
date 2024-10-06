package com.vadimraspopin.mangotest.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.vadimraspopin.mangotest.model.Chat
import com.vadimraspopin.mangotest.viewmodel.ChatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(viewModel: ChatsViewModel = ChatsViewModel(), onChatClicked: (Int) -> Unit = {}) {
    val chats = viewModel.chats

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Чаты") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding
        ) {
            items(items = chats) { chat ->
                ChatItem(chat = chat, onClick = { onChatClicked(chat.id) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(chat.name)
        },
        supportingContent = {
            Text(
                chat.lastMessage,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

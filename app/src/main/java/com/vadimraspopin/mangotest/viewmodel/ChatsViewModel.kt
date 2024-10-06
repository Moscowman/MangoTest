package com.vadimraspopin.mangotest.viewmodel

import androidx.lifecycle.ViewModel
import com.vadimraspopin.mangotest.model.Chat

class ChatsViewModel : ViewModel() {
    val chats = listOf(
        Chat(1, "Алексей", "До встречи!"),
        Chat(2, "Мария", "Спасибо за помощь."),
        Chat(3, "Команда разработки", "Новый релиз готов."),
        Chat(4, "Пётр", "Как проходят выходные?")
    )
}

package com.vadimraspopin.mangotest.viewmodel

import androidx.lifecycle.ViewModel
import com.vadimraspopin.mangotest.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    val messages = listOf(
        Message(1, "Привет!", false),
        Message(2, "Как дела?", false),
        Message(3, "Привет! Всё хорошо, спасибо. У тебя как?", true),
        Message(4, "Тоже отлично!", false)
    )
}

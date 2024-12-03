package com.autoever.jamanchu.models

data class ChatRoom(
    val lastMessage: String = "",
    val lastMessageTimeStamp: Long = 0L,
    val participants: List<String> = emptyList()
)

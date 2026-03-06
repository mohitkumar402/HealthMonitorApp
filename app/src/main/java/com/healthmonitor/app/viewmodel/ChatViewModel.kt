package com.healthmonitor.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthmonitor.app.data.models.ActivityData
import com.healthmonitor.app.data.models.ChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow(listOf(
        ChatMessage(1, "How many calories did I burn yesterday?", true, "2 hours ago"),
        ChatMessage(2, "You burned 420 kcal across 3 activities", false, "2 hours ago",
            activityBreakdown = listOf(
                ActivityData("Running", 120, 0.29f),
                ActivityData("Push up", 200, 0.48f),
                ActivityData("Sit ups", 100, 0.24f)
            )),
        ChatMessage(3, "Remind me to stretch at 8 PM", true, "1 hour ago"),
        ChatMessage(4, "Reminder set! I'll notify you at 8 PM to stretch. It improves flexibility!", false, "1 hour ago")
    ))
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    fun updateInput(text: String) { _inputText.value = text }

    fun sendMessage(text: String = _inputText.value) {
        if (text.isBlank()) return
        _messages.value = _messages.value + ChatMessage(_messages.value.size + 1, text, true, "Just now")
        _inputText.value = ""
        _isTyping.value = true
        viewModelScope.launch {
            delay(1200)
            _messages.value = _messages.value + ChatMessage(
                _messages.value.size + 2, generateResponse(text), false, "Just now"
            )
            _isTyping.value = false
        }
    }

    private fun generateResponse(input: String): String {
        val lower = input.lowercase()
        return when {
            "calori" in lower -> "Based on today, you've burned 320 kcal. Keep it up!"
            "water" in lower || "drink" in lower -> "You've had 1.5L today. Goal: 3L. Stay hydrated!"
            "step" in lower -> "2,340 steps done! You're 45% toward your 8,000 goal."
            "workout" in lower || "exercise" in lower -> "Workout logged! Great effort today!"
            "sleep" in lower -> "You slept ~7 hours last night. Good rest!"
            "stretch" in lower || "remind" in lower -> "Reminder set for stretching! Flexibility improves by 30% with regular stretching."
            else -> "I'm your health AI! Ask me about calories, steps, water, sleep or workouts."
        }
    }
}

package com.healthmonitor.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.healthmonitor.app.data.models.ActivityData
import com.healthmonitor.app.ui.components.CircularProgressBar
import com.healthmonitor.app.ui.theme.*
import com.healthmonitor.app.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
    val messages  by viewModel.messages.collectAsStateWithLifecycle()
    val isTyping  by viewModel.isTyping.collectAsStateWithLifecycle()
    val inputText by viewModel.inputText.collectAsStateWithLifecycle()
    val keyboard  = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.statusBarsPadding())
            // Title bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("AI Health Coach", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
            }
            Divider(color = Color(0xFFEEEEEE))

            // Messages list
            LazyColumn(
                state             = listState,
                modifier          = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding    = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    if (msg.isUser) {
                        // User bubble (right aligned)
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                Box(
                                    modifier = Modifier
                                        .widthIn(max = 260.dp)
                                        .clip(RoundedCornerShape(18.dp, 4.dp, 18.dp, 18.dp))
                                        .background(Color(0xFFEEEEEE))
                                        .padding(12.dp, 10.dp)
                                ) {
                                    Text(msg.text, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                                }
                                Spacer(Modifier.height(2.dp))
                                Text(msg.timestamp, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                    } else {
                        // AI bubble (left aligned)
                        Column {
                            Box(
                                modifier = Modifier
                                    .widthIn(max = 280.dp)
                                    .clip(RoundedCornerShape(4.dp, 18.dp, 18.dp, 18.dp))
                                    .background(Color.White)
                                    .padding(12.dp, 10.dp)
                            ) {
                                Text(msg.text, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            }
                            // Activity breakdown cards
                            if (msg.activityBreakdown != null) {
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    msg.activityBreakdown.forEach { act ->
                                        ActivityMiniCard(act)
                                    }
                                }
                            }
                            Spacer(Modifier.height(2.dp))
                            Text(msg.timestamp, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }

                if (isTyping) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color.White)
                                    .padding(14.dp, 10.dp)
                            ) {
                                Text("...", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            // Quick action chips
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Start workout", "Log water", "How did I sleep?", "My steps today").forEach { chip ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .clickable { viewModel.sendMessage(chip) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(chip, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }
                }
            }

            // Input bar
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp)
                    .navigationBarsPadding()
                    .padding(bottom = 72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value         = inputText,
                    onValueChange = viewModel::updateInput,
                    modifier      = Modifier.weight(1f),
                    placeholder   = { Text("Type something...", color = TextSecondary) },
                    shape         = RoundedCornerShape(30.dp),
                    singleLine    = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        viewModel.sendMessage()
                        keyboard?.hide()
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = OrangeAccent,
                        unfocusedBorderColor = Color(0xFFDDDDDD)
                    )
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(OrangeAccent)
                        .clickable {
                            viewModel.sendMessage()
                            keyboard?.hide()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun ActivityMiniCard(activity: ActivityData) {
    Card(
        shape   = RoundedCornerShape(14.dp),
        colors  = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier          = Modifier.padding(10.dp, 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(activity.name, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text      = "${activity.calories}kcal",
                    style     = MaterialTheme.typography.bodySmall,
                    color     = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                CircularProgressBar(
                    progress    = activity.progress,
                    modifier    = Modifier.size(28.dp),
                    strokeWidth = 4f
                )
            }
        }
    }
}

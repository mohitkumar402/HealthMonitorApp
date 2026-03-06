package com.healthmonitor.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmonitor.app.ui.theme.*

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 24.dp, bottom = 110.dp)
    ) {
        // Avatar + Name
        Column(
            modifier            = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(OrangeAccent),
                contentAlignment = Alignment.Center
            ) {
                Text("M", fontSize = 36.sp, color = Color.White, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(12.dp))
            Text("Mohit", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("Health Enthusiast", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }

        Spacer(Modifier.height(24.dp))

        // Stats row
        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("2,340", "Steps")
                VerticalDivider(modifier = Modifier.height(40.dp))
                StatItem("320", "Calories")
                VerticalDivider(modifier = Modifier.height(40.dp))
                StatItem("1.5L", "Water")
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Settings", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column {
                SettingsRow(Icons.Filled.Notifications, "Notifications", OrangeAccent)
                Divider(color = Color(0xFFF2F2F2))
                SettingsRow(Icons.Filled.DarkMode, "Dark Mode", Color(0xFF555555))
                Divider(color = Color(0xFFF2F2F2))
                SettingsRow(Icons.Filled.Lock, "Privacy & Security", Color(0xFF2196F3))
                Divider(color = Color(0xFFF2F2F2))
                SettingsRow(Icons.Filled.Help, "Help & Support", TealAccent)
                Divider(color = Color(0xFFF2F2F2))
                SettingsRow(Icons.Filled.Feedback, "Feedback", Color(0xFF9C27B0))
            }
        }

        Spacer(Modifier.height(16.dp))

        // Log out button
        Button(
            onClick  = {},
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape    = RoundedCornerShape(16.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF0EC))
        ) {
            Icon(Icons.Filled.ExitToApp, contentDescription = null, tint = OrangeAccent)
            Spacer(Modifier.width(8.dp))
            Text("Log Out", color = OrangeAccent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}

@Composable
private fun SettingsRow(icon: ImageVector, label: String, iconTint: Color) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, modifier = Modifier.weight(1f))
        Icon(Icons.Filled.ChevronRight, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
    }
}

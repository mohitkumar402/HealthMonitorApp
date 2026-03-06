package com.healthmonitor.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.healthmonitor.app.ui.components.CircularProgressBar
import com.healthmonitor.app.ui.components.toFormattedString
import com.healthmonitor.app.ui.theme.*
import com.healthmonitor.app.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onWorkoutClick: () -> Unit = {}
) {
    val stats by viewModel.dailyStats.collectAsStateWithLifecycle()

    val progress = stats.steps.toFloat() / stats.targetSteps
    val animatedProgress by animateFloatAsState(
        targetValue   = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label         = "dashProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 110.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text  = "Let's start\nstrong!",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Black,
                lineHeight = 44.sp
            ),
            color     = TextPrimary,
            textAlign = TextAlign.Center,
            modifier  = Modifier.fillMaxWidth().padding(vertical = 20.dp)
        )

        // Daily Goal Card
        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = "You're ${(animatedProgress * 100).toInt()}% to your daily goal",
                        style      = MaterialTheme.typography.titleMedium,
                        color      = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.88f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(ProgressTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(OrangeAccent)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(0.88f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text  = "${stats.steps.toFormattedString()}/${stats.targetSteps.toFormattedString()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                PulsingOrangeButton()
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickBtn(Icons.Filled.FitnessCenter, "Workout", onWorkoutClick)
            QuickBtn(Icons.Filled.Restaurant,    "Meal",    {})
            QuickBtn(Icons.Filled.WaterDrop,     "Water",   { viewModel.logWater() })
            QuickBtn(Icons.Filled.Sync,          "Sync",    {})
        }

        Spacer(Modifier.height(28.dp))
        Text(
            text  = "Daily Summary",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(Modifier.height(14.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier  = Modifier.weight(1f),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Steps", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier              = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text       = stats.steps.toFormattedString(),
                            style      = MaterialTheme.typography.headlineMedium,
                            color      = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        CircularProgressBar(
                            progress     = stats.steps.toFloat() / stats.targetSteps,
                            modifier     = Modifier.size(44.dp),
                            strokeWidth  = 6f,
                            animDuration = 1400
                        )
                    }
                }
            }

            Card(
                modifier  = Modifier.weight(1f),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Calories Burned", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text       = "${stats.caloriesBurned} kcal",
                        style      = MaterialTheme.typography.headlineMedium,
                        color      = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Water Intake", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text       = "${stats.waterIntake}L / ${stats.waterTarget}L",
                        style      = MaterialTheme.typography.titleMedium,
                        color      = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CircularProgressBar(
                        progress      = stats.waterIntake / stats.waterTarget,
                        modifier      = Modifier.size(44.dp),
                        progressColor = Color(0xFF2196F3),
                        strokeWidth   = 6f
                    )
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2196F3))
                            .clickable { viewModel.logWater() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PulsingOrangeButton() {
    val infinite = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infinite.animateFloat(
        initialValue  = 1f,
        targetValue   = 1.18f,
        animationSpec = infiniteRepeatable(
            animation  = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infinite.animateFloat(
        initialValue  = 0.30f,
        targetValue   = 0f,
        animationSpec = infiniteRepeatable(
            animation  = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(OrangeAccent.copy(alpha = pulseAlpha))
        )
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(OrangeAccent)
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Bolt, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun QuickBtn(icon: ImageVector, label: String, onClick: () -> Unit) {
    val source    = remember { MutableInteractionSource() }
    val isPressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 0.90f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label         = "btnScale"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .scale(scale)
                .shadow(4.dp, CircleShape)
                .size(62.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(
                    interactionSource = source,
                    indication        = ripple(bounded = true, color = OrangeAccent),
                    onClick           = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = TextPrimary, modifier = Modifier.size(26.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}

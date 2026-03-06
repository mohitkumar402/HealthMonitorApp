package com.healthmonitor.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.healthmonitor.app.data.models.WeeklyData
import com.healthmonitor.app.ui.components.toFormattedString
import com.healthmonitor.app.ui.theme.*
import com.healthmonitor.app.viewmodel.ProgressViewModel

@Composable
fun ProgressScreen(viewModel: ProgressViewModel = viewModel()) {
    val selectedTab      by viewModel.selectedTab.collectAsStateWithLifecycle()
    val exerciseProgress by viewModel.exerciseProgress.collectAsStateWithLifecycle()
    val weeklyData       by viewModel.weeklyData.collectAsStateWithLifecycle()
    val showTip          by viewModel.showTip.collectAsStateWithLifecycle()

    val animExercise by animateFloatAsState(
        targetValue   = exerciseProgress.exerciseMinutes.toFloat() / exerciseProgress.exerciseTarget,
        animationSpec = tween(1400, easing = FastOutSlowInEasing),
        label         = "exerciseRing"
    )
    val animStand by animateFloatAsState(
        targetValue   = exerciseProgress.standMinutes.toFloat() / exerciseProgress.standTarget,
        animationSpec = tween(1600, easing = FastOutSlowInEasing),
        label         = "standRing"
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
        Spacer(Modifier.height(8.dp))

        // Tab row
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFFE8E8E8))
                .padding(4.dp)
        ) {
            listOf("Daily", "Weekly", "Monthly").forEachIndexed { i, label ->
                val isSelected = selectedTab == i
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(26.dp))
                        .background(if (isSelected) Color.White else Color.Transparent)
                        .clickable { viewModel.selectTab(i) }
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = label,
                        style      = MaterialTheme.typography.bodyMedium,
                        color      = if (isSelected) TextPrimary else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Orange tip card
        if (showTip) {
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = OrangeAccent),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text       = "Stretching after workouts improves your sleep quality.",
                        style      = MaterialTheme.typography.titleMedium,
                        color      = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick  = { viewModel.dismissTip() },
                            border   = BorderStroke(1.5.dp, Color.White),
                            shape    = RoundedCornerShape(30.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Dismiss", color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick  = {},
                            colors   = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape    = RoundedCornerShape(30.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Set Routine", color = OrangeAccent, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }

        // Activity rings card
        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier          = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActivityRingsCanvas(exerciseFrac = animExercise, standFrac = animStand, modifier = Modifier.size(110.dp))
                Spacer(Modifier.width(20.dp))
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    ActivityLegend("Exercise", exerciseProgress.exerciseMinutes, exerciseProgress.exerciseTarget, OrangeAccent)
                    ActivityLegend("Stand",    exerciseProgress.standMinutes,    exerciseProgress.standTarget,    Color(0xFF333333))
                }
            }
        }

        Spacer(Modifier.height(16.dp))

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
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text       = weeklyData.find { it.isToday }?.steps?.toFormattedString() ?: "2.340",
                        style      = MaterialTheme.typography.headlineMedium,
                        color      = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    AnimatedBarChart(weeklyData, modifier = Modifier.fillMaxWidth().height(60.dp))
                }
            }

            Card(
                modifier  = Modifier.weight(1f),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Distance", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "3.4km", style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary, fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(0f, size.height * 0.5f)
                            lineTo(size.width * 0.2f, size.height * 0.2f)
                            lineTo(size.width * 0.4f, size.height * 0.6f)
                            lineTo(size.width * 0.6f, size.height * 0.3f)
                            lineTo(size.width * 0.8f, size.height * 0.7f)
                            lineTo(size.width, size.height * 0.4f)
                        }
                        drawPath(path, OrangeAccent, style = Stroke(4f, cap = StrokeCap.Round,
                            join = androidx.compose.ui.graphics.StrokeJoin.Round))
                        listOf(
                            size.width * 0.2f to size.height * 0.2f,
                            size.width * 0.6f to size.height * 0.3f
                        ).forEach { (x, y) ->
                            drawCircle(OrangeAccent, 6f, androidx.compose.ui.geometry.Offset(x, y))
                            drawCircle(Color.White,  3f, androidx.compose.ui.geometry.Offset(x, y))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityRingsCanvas(exerciseFrac: Float, standFrac: Float, modifier: Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val sw   = size.minDimension * 0.13f
        val pad1 = sw / 2f
        val pad2 = sw * 1.8f
        drawArc(Color(0xFFEEEEEE), -90f, 360f, false, Stroke(sw, cap = StrokeCap.Round),
            topLeft = androidx.compose.ui.geometry.Offset(pad1, pad1),
            size    = androidx.compose.ui.geometry.Size(size.width - pad1*2, size.height - pad1*2))
        if (exerciseFrac > 0f)
            drawArc(OrangeAccent, -90f, exerciseFrac * 360f, false, Stroke(sw, cap = StrokeCap.Round),
                topLeft = androidx.compose.ui.geometry.Offset(pad1, pad1),
                size    = androidx.compose.ui.geometry.Size(size.width - pad1*2, size.height - pad1*2))
        drawArc(Color(0xFFEEEEEE), -90f, 360f, false, Stroke(sw, cap = StrokeCap.Round),
            topLeft = androidx.compose.ui.geometry.Offset(pad2, pad2),
            size    = androidx.compose.ui.geometry.Size(size.width - pad2*2, size.height - pad2*2))
        if (standFrac > 0f)
            drawArc(Color(0xFF333333), -90f, standFrac * 360f, false, Stroke(sw, cap = StrokeCap.Round),
                topLeft = androidx.compose.ui.geometry.Offset(pad2, pad2),
                size    = androidx.compose.ui.geometry.Size(size.width - pad2*2, size.height - pad2*2))
    }
}

@Composable
private fun ActivityLegend(label: String, current: Int, target: Int, dotColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(dotColor))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Text("$current/${target}min", style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AnimatedBarChart(data: List<WeeklyData>, modifier: Modifier) {
    val maxSteps = data.maxOfOrNull { it.steps }?.toFloat() ?: 1f
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
        data.forEachIndexed { i, item ->
            val anim by animateFloatAsState(
                targetValue   = item.steps / maxSteps,
                animationSpec = tween(800 + i * 80, easing = FastOutSlowInEasing),
                label         = "bar$i"
            )
            Box(
                modifier = Modifier
                    .width(9.dp)
                    .fillMaxHeight(anim)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(if (item.isToday) OrangeAccent else Color(0xFFDDDDDD))
            )
        }
    }
}

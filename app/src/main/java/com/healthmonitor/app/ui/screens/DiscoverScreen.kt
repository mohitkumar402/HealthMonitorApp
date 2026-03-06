package com.healthmonitor.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmonitor.app.data.models.NutritionData
import com.healthmonitor.app.data.models.ScheduledWorkout
import com.healthmonitor.app.data.models.WorkoutItem
import com.healthmonitor.app.ui.theme.*

private val sampleWorkouts = listOf(
    WorkoutItem(1, "Pull Force 💪", "Strengthen your back & biceps.", "Strength", 4.8f, bgColors = listOf(0xFF1A2840, 0xFF0D3B6B)),
    WorkoutItem(2, "Full Body Burn 🔥", "AI-crafted workout combining push, pull & legs.", "Full Body", 5.0f, bgColors = listOf(0xFF0A4D4A, 0xFF006B65)),
    WorkoutItem(3, "Leg Blast ⚡", "Power up your lower body.", "Legs", 4.5f, bgColors = listOf(0xFF3A1A4D, 0xFF5C0D6B)),
    WorkoutItem(4, "Core Crusher", "Build a strong and stable core.", "Core", 4.7f, bgColors = listOf(0xFF4D2A0A, 0xFF6B3E0D))
)

private val scheduledWorkouts = listOf(
    ScheduledWorkout("Bench Press", 2, "10-12 reps", 0.85f),
    ScheduledWorkout("Deadlift",    2, "10-12 reps", 1.00f)
)

@Composable
fun DiscoverScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(top = 16.dp, bottom = 110.dp)
    ) {
        // Header
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TealAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Text("C", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.width(8.dp))
                Text("CerbroFit", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(Icons.Filled.Search,        null, tint = Color.White, modifier = Modifier.size(24.dp))
                Icon(Icons.Filled.Notifications, null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }

        // Discover title
        Text(
            text       = "Discover",
            fontSize   = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = Color.White,
            modifier   = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        // Horizontal workout cards
        LazyRow(
            contentPadding        = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sampleWorkouts) { workout ->
                WorkoutCard(workout, isFeatured = workout.id == 2)
            }
        }

        Spacer(Modifier.height(24.dp))

        // Bottom two-panel row
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Daily Progress donut
            DailyProgressCard(NutritionData(), modifier = Modifier.weight(1f))

            // Schedule calendar
            ScheduleCard(scheduledWorkouts, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun WorkoutCard(workout: WorkoutItem, isFeatured: Boolean) {
    val w = if (isFeatured) 240.dp else 180.dp
    Box(
        modifier = Modifier
            .width(w)
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(workout.bgColors.map { Color(it) })
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Rating
            if (isFeatured) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, null, tint = YellowAccent, modifier = Modifier.size(16.dp))
                    Text(" ${workout.rating}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                }
            }
            Column {
                Text(workout.title, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(workout.description, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                if (isFeatured) {
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {},
                        colors  = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape   = RoundedCornerShape(30.dp)
                    ) {
                        Text("Try Now", color = Color(0xFF0A4D4A), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyProgressCard(nutrition: NutritionData, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = DarkCard),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Daily Progress", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            // Donut chart
            Box(
                modifier         = Modifier.size(100.dp).align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    val total       = (nutrition.proteinKcal + nutrition.carbsKcal + nutrition.fatKcal).toFloat()
                    val strokeWidth = size.minDimension * 0.16f
                    val segments    = listOf(
                        TealAccent to nutrition.proteinKcal / total,
                        OrangeAccent to nutrition.carbsKcal / total,
                        YellowAccent to nutrition.fatKcal / total
                    )
                    var start = -90f
                    segments.forEach { (color, frac) ->
                        val sweep = frac * 360f
                        drawArc(color = color, startAngle = start, sweepAngle = sweep, useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt))
                        start += sweep
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${nutrition.totalCalories}", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Kcal", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.7f))
                }
            }

            Spacer(Modifier.height(12.dp))
            NutritionRow("Protein", TealAccent,   "${nutrition.proteinKcal}kcal")
            NutritionRow("Carbo",   OrangeAccent, "${nutrition.carbsKcal}kcal")
            NutritionRow("Fat",     YellowAccent, "${nutrition.fatKcal}kcal")

            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(TealAccent.copy(0.15f))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, null, tint = TealAccent, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("You've gained 2kg in a month!", style = MaterialTheme.typography.bodySmall, color = TealAccent)
                }
            }
        }
    }
}

@Composable
private fun NutritionRow(label: String, color: Color, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
            Spacer(Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.7f))
        }
        Text(value, style = MaterialTheme.typography.bodySmall, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ScheduleCard(workouts: List<ScheduledWorkout>, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = DarkCard),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("September 2025", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))

            // Week row
            val days = listOf("M", "T", "W", "T", "F", "S", "S")
            val nums = listOf(15, 16, 17, 18, 19, 20, 21)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                days.forEachIndexed { i, d ->
                    val isToday = i == 3
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(d, style = MaterialTheme.typography.bodySmall, color = if (isToday) TealAccent else Color.White.copy(0.5f))
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier         = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(if (isToday) TealAccent else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${nums[i]}", style = MaterialTheme.typography.bodySmall,
                                color = if (isToday) Color.White else Color.White.copy(0.7f),
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            workouts.forEach { workout ->
                WorkoutScheduleItem(workout)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun WorkoutScheduleItem(workout: ScheduledWorkout) {
    Card(
        shape   = RoundedCornerShape(12.dp),
        colors  = CardDefaults.cardColors(containerColor = Color(0xFF1E2E44)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play button placeholder
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2A3E58)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(workout.name, style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text("${workout.sets} Sets · ${workout.reps}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.6f))
            }
            // Progress circle
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (workout.progress >= 1f) TealAccent
                        else OrangeAccent.copy(0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text      = "${(workout.progress * 100).toInt()}%",
                    style     = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp),
                    color     = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

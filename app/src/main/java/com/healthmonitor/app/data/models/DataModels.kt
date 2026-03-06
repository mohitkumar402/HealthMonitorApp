package com.healthmonitor.app.data.models

data class DailyStats(
    val steps: Int = 2340,
    val targetSteps: Int = 8000,
    val caloriesBurned: Int = 320,
    val waterIntake: Float = 1.5f,
    val waterTarget: Float = 3.0f,
    val exerciseMinutes: Int = 45,
    val distanceKm: Float = 3.4f
)

data class WorkoutItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val rating: Float,
    val duration: String = "45 min",
    val bgColors: List<Long> = listOf(0xFF162235, 0xFF1A3A5C)
)

data class ChatMessage(
    val id: Int,
    val text: String,
    val isUser: Boolean,
    val timestamp: String,
    val activityBreakdown: List<ActivityData>? = null
)

data class ActivityData(
    val name: String,
    val calories: Int,
    val progress: Float
)

data class NutritionData(
    val totalCalories: Int = 1200,
    val proteinKcal: Int = 240,
    val carbsKcal: Int = 420,
    val fatKcal: Int = 540
)

data class ExerciseProgress(
    val exerciseMinutes: Int = 240,
    val exerciseTarget: Int = 600,
    val standMinutes: Int = 160,
    val standTarget: Int = 600
)

data class WeeklyData(
    val day: String,
    val steps: Int,
    val isToday: Boolean = false
)

data class ScheduledWorkout(
    val name: String,
    val sets: Int,
    val reps: String,
    val progress: Float
)

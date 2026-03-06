package com.healthmonitor.app.viewmodel

import androidx.lifecycle.ViewModel
import com.healthmonitor.app.data.models.DailyStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {

    private val _dailyStats = MutableStateFlow(DailyStats())
    val dailyStats: StateFlow<DailyStats> = _dailyStats.asStateFlow()

    fun logWater() {
        val c = _dailyStats.value
        _dailyStats.value = c.copy(waterIntake = (c.waterIntake + 0.25f).coerceAtMost(c.waterTarget))
    }
}

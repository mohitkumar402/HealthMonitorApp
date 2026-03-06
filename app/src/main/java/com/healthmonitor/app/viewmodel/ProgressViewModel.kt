package com.healthmonitor.app.viewmodel

import androidx.lifecycle.ViewModel
import com.healthmonitor.app.data.models.ExerciseProgress
import com.healthmonitor.app.data.models.WeeklyData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressViewModel : ViewModel() {

    private val _selectedTab = MutableStateFlow(1)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _exerciseProgress = MutableStateFlow(ExerciseProgress())
    val exerciseProgress: StateFlow<ExerciseProgress> = _exerciseProgress.asStateFlow()

    private val _weeklyData = MutableStateFlow(listOf(
        WeeklyData("Mon", 3200),
        WeeklyData("Tue", 5100),
        WeeklyData("Wed", 4200),
        WeeklyData("Thu", 2340, isToday = true),
        WeeklyData("Fri", 6800),
        WeeklyData("Sat", 7200),
        WeeklyData("Sun", 1500)
    ))
    val weeklyData: StateFlow<List<WeeklyData>> = _weeklyData.asStateFlow()

    private val _showTip = MutableStateFlow(true)
    val showTip: StateFlow<Boolean> = _showTip.asStateFlow()

    fun selectTab(index: Int) { _selectedTab.value = index }
    fun dismissTip() { _showTip.value = false }
}

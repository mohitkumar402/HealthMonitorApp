package com.healthmonitor.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.healthmonitor.app.ui.screens.*
import com.healthmonitor.app.ui.theme.DarkNavBg
import com.healthmonitor.app.ui.theme.TextPrimary

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Progress  : Screen("progress")
    object Chat      : Screen("chat")
    object Discover  : Screen("discover")
    object Profile   : Screen("profile")
}

private data class NavItem(val screen: Screen, val icon: ImageVector, val label: String)

private val navItems = listOf(
    NavItem(Screen.Dashboard, Icons.Filled.Home,     "Dashboard"),
    NavItem(Screen.Progress,  Icons.Filled.BarChart, "Progress"),
    NavItem(Screen.Chat,      Icons.Filled.Chat,     "AI Chat"),
    NavItem(Screen.Discover,  Icons.Filled.Explore,  "Discover"),
    NavItem(Screen.Profile,   Icons.Filled.Person,   "Profile")
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentEntry  by navController.currentBackStackEntryAsState()
    val currentRoute  = currentEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController    = navController,
            startDestination = Screen.Dashboard.route,
            modifier         = Modifier.fillMaxSize(),
            enterTransition  = {
                fadeIn(tween(280)) + slideInHorizontally(tween(280, easing = FastOutSlowInEasing)) { it / 6 }
            },
            exitTransition   = {
                fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 6 }
            },
            popEnterTransition = {
                fadeIn(tween(280)) + slideInHorizontally(tween(280)) { -it / 6 }
            },
            popExitTransition  = {
                fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { it / 6 }
            }
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(onWorkoutClick = { navController.navigate(Screen.Discover.route) })
            }
            composable(Screen.Progress.route)  { ProgressScreen() }
            composable(Screen.Chat.route)      { ChatScreen() }
            composable(Screen.Discover.route)  { DiscoverScreen() }
            composable(Screen.Profile.route)   { ProfileScreen() }
        }

        BottomNavBar(
            currentRoute = currentRoute,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .navigationBarsPadding(),
            onItemClick = { screen ->
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState    = true
                }
            }
        )
    }
}

@Composable
private fun BottomNavBar(
    currentRoute: String?,
    modifier: Modifier = Modifier,
    onItemClick: (Screen) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(40.dp))
            .background(DarkNavBg)
            .padding(horizontal = 6.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        navItems.forEach { item ->
            val isSelected = currentRoute == item.screen.route

            // Animate icon scale on selection
            val iconScale by animateFloatAsState(
                targetValue   = if (isSelected) 1.1f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label         = "iconScale"
            )

            AnimatedContent(
                targetState   = isSelected,
                transitionSpec = {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(150))
                },
                label = "navItem_${item.label}"
            ) { selected ->
                if (selected) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color.White)
                            .clickable { onItemClick(item.screen) }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            tint     = TextPrimary,
                            modifier = Modifier.size(18.dp).scale(iconScale)
                        )
                        Text(item.label, style = MaterialTheme.typography.labelLarge, color = TextPrimary)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable { onItemClick(item.screen) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            tint     = Color.White.copy(alpha = 0.55f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

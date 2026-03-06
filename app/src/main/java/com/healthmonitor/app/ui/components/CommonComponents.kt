package com.healthmonitor.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.healthmonitor.app.ui.theme.OrangeAccent

/**
 * Animated circular progress bar — sweeps from 0 to [progress] on first composition.
 */
@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    trackColor: Color = Color(0xFFEEEEEE),
    progressColor: Color = OrangeAccent,
    strokeWidth: Float = 6f,
    animDuration: Int = 1000
) {
    val animatedProgress by animateFloatAsState(
        targetValue    = progress.coerceIn(0f, 1f),
        animationSpec  = tween(durationMillis = animDuration, easing = FastOutSlowInEasing),
        label          = "circularProgress"
    )

    Canvas(modifier = modifier) {
        drawArc(
            color      = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter  = false,
            style      = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        if (animatedProgress > 0f) {
            drawArc(
                color      = progressColor,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter  = false,
                style      = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}

fun Int.toFormattedString(): String = "%,d".format(this).replace(",", ".")

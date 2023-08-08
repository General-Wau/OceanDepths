package com.necromyd.oceandepths

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimatedCloud(
    cloudImage: Painter,
    cloudSize: Dp = 64.dp,
    screenHeight: Dp = 700.dp,
    animationDuration: Long = 3000L
) {
    var cloudX by remember { mutableStateOf(0.dp) }
    var cloudY by remember { mutableStateOf(0.dp) }

    val screenWidth = with(LocalDensity.current) {
        LocalContext.current.resources.displayMetrics.widthPixels.dp / density
    }

    val screenHeightInt = screenHeight.value.toInt()



    val cloudXAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            cloudY = (0..(screenHeightInt - cloudSize.value.toInt())).random().dp

            cloudXAnimation.animateTo(
                targetValue = screenWidth.value + cloudSize.value,
                animationSpec = tween(durationMillis = animationDuration.toInt(), easing = LinearEasing)
            )

            // Reset the position of the cloud to the starting position
            cloudXAnimation.snapTo((-cloudSize).value)

            // Wait for a short duration before respawning the cloud
            delay(1000L)
        }
    }


    Box(
        modifier = Modifier
            .width(cloudSize)
            .height(cloudSize)
            .offset(x = cloudX, y = cloudY)
    ) {
        Image(painter = cloudImage, contentDescription = null)
    }
}
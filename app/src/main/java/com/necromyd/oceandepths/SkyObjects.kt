package com.necromyd.oceandepths

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * This composable handles moving clouds in the sky box within OceanDepth composable
 */
@Composable
fun Cloud() {
    val infiniteTransition = rememberInfiniteTransition()
    var currentY by remember { mutableStateOf(Random.nextInt(0, 500).toFloat()) }

    val xPosition by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 450f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 10000
                0f at -100 with  LinearEasing
                450f at 10000
            } ,
            repeatMode = RepeatMode.Restart
        )
    )

    LaunchedEffect(xPosition) {
        if (xPosition >= 440f) {
            currentY = Random.nextInt(0, 500).toFloat()
        }
    }

    Image(
        painter = painterResource(id = R.drawable.cloud),
        contentDescription = null,
        modifier = Modifier
            .offset(x = xPosition.dp, y = currentY.dp)
            .size(100.dp)
    )
}
package com.necromyd.oceandepths

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.necromyd.oceandepths.ui.theme.OceanDepthsTheme

lateinit var viewModel: OceanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = OceanViewModel()

        setContent {
            OceanDepthsTheme {
                OceanDepthApp()
                DepthMeter()
            }
        }
    }
}

@Composable
fun DepthMeter() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.BottomEnd
    ) {
        Text(
            text = viewModel.getDepth().toInt().toString() + "m",
            color = Color.White,
            fontSize = 40.sp
        )
    }

}

@Composable
fun OceanDepthApp() {
    val verticalScrollState = rememberScrollState()
    val skyColor = Color(0xFF70B5FA)

    Column(
        modifier = Modifier
            .verticalScroll(verticalScrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(720.dp)
                .background(skyColor)
        ) {
            // Content of the first box (e.g., landmarks, sea creatures, etc.)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80000.dp)
                .background(
                    brush = verticalGradientBackground(gradientPercentage = 0.11f)
                )
        ) {
            // Content of the second box (e.g., landmarks, sea creatures, etc.)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.DarkGray)
        ) {
            // Content of the second box (e.g., landmarks, sea creatures, etc.)
        }
        LaunchedEffect(verticalScrollState.value) {
            viewModel.scrollPosition.value = verticalScrollState.value
        }
    }
}

@Composable
fun verticalGradientBackground(gradientPercentage: Float): Brush {
    val gradientHeightOcean = 80000.dp * gradientPercentage

    return Brush.verticalGradient(
        colors = listOf(Color.Blue, Color.Black),
        startY = 0f,
        endY = with(LocalDensity.current) { gradientHeightOcean.toPx() }
    )


}

@Composable
fun TitleScreenContent() {
    var showContent by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val density = LocalDensity.current
    val customButtonColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.8f)

    if (showContent) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Ocean Depths",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(110.dp))
                Button(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    onClick = {
                        showContent = !showContent
                    },
                    shape = RoundedCornerShape(20.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 15.dp,
                        disabledElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(customButtonColor)
                ) {
                    Text(text = "Begin", fontSize = 25.sp)
                }
                // Add any other UI elements or text at the bottom as needed.
                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "App by Necromyd , 2023",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OceanDepthsTheme {
        DepthMeter()
        OceanDepthApp()
    }
}
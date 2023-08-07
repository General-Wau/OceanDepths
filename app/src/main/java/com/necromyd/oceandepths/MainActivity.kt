package com.necromyd.oceandepths

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.necromyd.oceandepths.ui.theme.OceanDepthsTheme
import kotlinx.coroutines.delay

lateinit var viewModel: OceanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = OceanViewModel()

        setContent {
            OceanDepthsTheme {
                OceanDepthsApp()
            }
        }
    }
}

@Composable
fun OceanDepthsApp() {
    val verticalScrollState = rememberScrollState()

    Box {
        OceanDepthApp(verticalScrollState)
        DepthMeter(verticalScrollState)
        BlinkingTextComposable()
        TitleScreenContent()
    }

}

@Composable
fun DepthMeter(verticalScrollState: ScrollState) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.BottomEnd
    ) {
        Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
            Image(painter = painterResource(id = R.drawable.sub),
                contentDescription = "Submarine image button, click to scroll up",
                modifier = Modifier
                    .size(130.dp)
                    .clickable {
                        coroutineScope.launch {
                            verticalScrollState.animateScrollTo(0)
                        }
                    })
            Text(
                text = viewModel.getDepth().toInt().toString() + "m",
                color = Color.White,
                fontSize = 40.sp
            )
        }

    }
}


@Composable
fun OceanDepthApp(verticalScrollState: ScrollState) {
    val skyColor = Color(0xFF70B5FA)


    Column(
        modifier = if (viewModel.isScrollEnabled.value) Modifier
            .verticalScroll(verticalScrollState) else Modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
                .background(skyColor)
        ) {
            // Content of the first box (e.g., landmarks, sea creatures, etc.)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80000.dp)
                .background(
                    brush = verticalGradientBackground(gradientPercentage = 0.093f)
                )
        ) {
            // Content of the second box (e.g., landmarks, sea creatures, etc.)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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
fun BlinkingText(
    text: String,
    blinkingSpeed: Long = 700L,
    textSize: TextUnit = 32.sp,
    textColor: Color = Color.White
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            visible = !visible
            delay(blinkingSpeed)
        }
    }

    if (visible) {
        Text(
            text = text,
            fontSize = textSize,
            color = textColor
        )
    }
}

@Composable
fun BlinkingTextComposable() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(viewModel.isScrollEnabled.value && viewModel.scrollPosition.value < 750){
                BlinkingText(
                    text = "Scroll down and dive!"
                )
            }
        }
    }
}

@Composable
fun TitleScreenContent() {
    var showContent by remember { mutableStateOf(true) }
    val customButtonColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.4f)

    if (showContent) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painterResource(id = R.drawable.title),
                    contentDescription = "How deep is the ocean?",
                    modifier = Modifier.size(370.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    onClick = {
                        showContent = !showContent
                        viewModel.isScrollEnabled.value = true
                    },
                    shape = RoundedCornerShape(40.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 15.dp,
                        disabledElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(customButtonColor)
                ) {
                    Text(text = "Find out!", fontSize = 25.sp, color = Color.White)
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
    }
}
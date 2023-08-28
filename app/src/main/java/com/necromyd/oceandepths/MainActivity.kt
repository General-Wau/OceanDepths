package com.necromyd.oceandepths

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.necromyd.oceandepths.ui.theme.OceanDepthsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

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

/**
 * The top composable that organizes the drawing order of all composables in the application
 */
@Composable
fun OceanDepthsApp() {
    val verticalScrollState = rememberScrollState()

    Box {
        OceanDepth(verticalScrollState)
        DepthMeter(verticalScrollState)
        BlinkingTextComposable()
        TitleScreenContent()
        if (viewModel.showTextPopUp) {
            TextPopUp()
        }
    }

}

/**
 * Description text that appears when an image in the ocean is clicked
 */
@Composable
fun TextPopUp() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.7f)
                .background(Color.Black.copy(alpha = 0.4f))
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
                .clickable {
                    viewModel.showTextPopUp = false
                }
        ) {
            if (viewModel.showTextPopUp) {
                viewModel.selectedImage?.let {
                    Column {
                        Text(
                            modifier = Modifier.padding(bottom = 10.dp),
                            text = it.title,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = it.text,
                            textAlign = TextAlign.Center, fontSize = 16.sp, color = Color.White
                        )
                    }
                }
            }
        }
        Text(
            text = stringResource(id = R.string.close_popup_text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = Color.Yellow
        )
    }
}

/**
 * This composable handles the ship image button and the text that displays current depth
 * It persists on the screen
 * @param verticalScrollState Current state of the vertical scroll
 */
@Composable
fun DepthMeter(verticalScrollState: ScrollState) {
    val coroutineScope = rememberCoroutineScope()

    val maxDepth = 10910
    val totalLayoutHeightDp = 70000.dp
    val totalLayoutHeightPx = with(LocalDensity.current) {
        totalLayoutHeightDp.toPx()
    }

    val normalizedScrollPosition = verticalScrollState.value / totalLayoutHeightPx
    viewModel.depth = normalizedScrollPosition * maxDepth

    val submarineImage = if (Locale.getDefault().language == "sr") {
        R.drawable.subrs
    } else {
        R.drawable.sub
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
            if (viewModel.isScrollEnabled) {
                Image(
                    painter = painterResource(submarineImage),
                    contentDescription = stringResource(id = R.string.submarine_button_description),
                    modifier = Modifier
                        .size(130.dp)
                        .clickable {
                            coroutineScope.launch {
                                verticalScrollState.animateScrollTo(0)
                            }
                        }
                )
                Text(
                    text = "${viewModel.depth.roundToInt()} m",
                    color = Color.White,
                    fontSize = 40.sp
                )
            }
        }
    }
}

/**
 * The primary composable of the application
 * It draws three boxes , sky , ocean and ground
 * @param verticalScrollState The current state of the vertical scroll
 */
@Composable
fun OceanDepth(verticalScrollState: ScrollState) {
    val skyColor = Color(0xFF70B5FA)
    var showSecondCloud by remember { mutableStateOf(false) }
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val skyBoxHeight = (screenHeight * 0.85f)

    Column(
        modifier = if (viewModel.isScrollEnabled) Modifier
            .verticalScroll(verticalScrollState) else Modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(skyBoxHeight)
                .background(skyColor)
        ) {
            if (viewModel.isScrollEnabled) {
                Cloud()
                LaunchedEffect(Unit) {
                    delay(Random.nextLong(3000, 7000))
                    showSecondCloud = true
                }
                if (showSecondCloud) {
                    Cloud()
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70000.dp)
                .background(
                    brush = verticalGradientBackground(gradientPercentage = 0.093f)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val coroutineScope = rememberCoroutineScope()
                var previousTopPadding = 0.dp

                viewModel.imageDataList.forEach { imageData ->

                    val isVisible = viewModel.isImageVisible(imageData)
                    val currentTopPadding = viewModel.positionImage(imageData)
                    val calculatedPadding = currentTopPadding - previousTopPadding

                    Spacer(modifier = Modifier.height(calculatedPadding))

                    if (imageData.resource != null && isVisible) {
                        // Display image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageData.size.dp)
                        ) {
                            Image(
                                painter = painterResource(id = imageData.resource),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        coroutineScope.launch {
                                            viewModel.selectedImage = imageData
                                            viewModel.showTextPopUp = true
                                        }
                                    },
                                contentScale = ContentScale.FillHeight
                            )
                        }
                    } else {
                        // Display text
                        Text(
                            text = imageData.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 140.dp, top = 0.dp, bottom = 0.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.selectedImage = imageData
                                        viewModel.showTextPopUp = true
                                    }
                                },
                            textAlign = TextAlign.Start,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }

                    previousTopPadding =
                        currentTopPadding + (if (imageData.resource != null) imageData.size.dp else 0.dp)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.DarkGray)
        ) {
        }
        LaunchedEffect(verticalScrollState.value) {
            viewModel.scrollPosition.value = verticalScrollState.value
        }
    }
}

/**
 * Draws the gradient of the ocean from blue to black
 * The gradient ends at 1000m
 * @param gradientPercentage This decides how far the gradient will go from blue to black
 * @return Returns the Brush object that will paint the gradient
 */
@Composable
fun verticalGradientBackground(gradientPercentage: Float): Brush {
    val gradientHeightOcean = 70000.dp * gradientPercentage

    return Brush.verticalGradient(
        colors = listOf(Color.Blue, Color.Black),
        startY = 0f,
        endY = with(LocalDensity.current) { gradientHeightOcean.toPx() }
    )

}

/**
 * Composable that handles blinking text.
 * It has a built in check to see if the composable is run for the first time,
 * to fix incomplete/un-synchronized blink that usually appears after the main screen.
 *
 * @param text The text String that will blink
 * @param blinkingSpeed The delay between each blink
 * @param textSize The size of the blinking text
 * @param textColor The color of the blinking text
 * @see BlinkingTextComposable() for application of this composable
 */
@Composable
fun BlinkingText(
    text: String,
    blinkingSpeed: Long = 700L,
    textSize: TextUnit = 32.sp,
    textColor: Color = Color.White
) {
    var visible by remember { mutableStateOf(true) }
    var firstRun by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            if (firstRun) {
                delay(500)
                firstRun = false
            }
            visible = !visible
            delay(blinkingSpeed)
        }
    }

    if (visible) {
        Text(
            text = text,
            fontSize = textSize,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Composable that displays the blinking text
 */
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
            if (viewModel.isScrollEnabled && viewModel.scrollPosition.value < 750) {
                BlinkingText(
                    text = stringResource(id = R.string.blinking_text)
                )
            }
        }
    }
}

/**
 * Composable that displays the title image and a button
 * It is drawn over everything and will hide most of the features until the button is pressed
 */
@Composable
fun TitleScreenContent() {
    var showContent by remember { mutableStateOf(true) }
    val customButtonColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.4f)
    val imageResId = if (Locale.getDefault().language == "sr") {
        R.drawable.title_sr
    } else {
        R.drawable.title_en
    }

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
                    painterResource(id = imageResId),
                    contentDescription = stringResource(id = R.string.main_title_description),
                    modifier = Modifier.size(370.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    onClick = {
                        showContent = !showContent
                        viewModel.isScrollEnabled = true
                    },
                    shape = RoundedCornerShape(40.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 15.dp,
                        disabledElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(customButtonColor)
                ) {
                    Text(
                        text = stringResource(id = R.string.main_button),
                        fontSize = 25.sp,
                        color = Color.White
                    )
                }
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
                    text = stringResource(id = R.string.credits),
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    OceanDepthsTheme {
//    }
//}
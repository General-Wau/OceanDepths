package com.necromyd.oceandepths

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

class OceanViewModel : ViewModel() {
    data class ImageData(
        val resource: Int?,
        val title: String,
        val text: String,
        val depth: Int,
        val size: Float
    )

    var scrollPosition = mutableStateOf(0)
    var isScrollEnabled by mutableStateOf(false)
    var showTextPopUp by mutableStateOf(false)
    var selectedImage: ImageData? by mutableStateOf(null)
    var depth by mutableStateOf(0f)

    val imageDataList = listOf(
        ImageData(
            null, "Midnight Zone",
            "The midnight zone spooky very very spooky spooky very very spookyspooky very very " +
                    "spookyspooky very very spookyspooky very very spookyspooky very very spooky" +
                    "spooky very very spookyspooky very very spookyspooky very very spookyspooky " +
                    "very very spookyspooky very very spookyspooky very very spooky",
            300,
            150f
        ),
        ImageData(
            R.drawable.cloud, "2nd cloud", "info info info info info info info info info " +
                    "info info info info info info info info info info info info " +
                    "info info info ", 2000, 100f
        ),
        ImageData(R.drawable.cloud, "3rd cloud", "info info info", 5000, 120f),
//        ImageData(R.drawable.cloud, "4th cloud", 205, 75f),
//        ImageData(R.drawable.cloud, "5th cloud", 3000, 170f)

    )

    /**
     * Function that decides if image should be visible
     * Only images that are within 500dp of the current scrolling position will be loaded
     * Hidden otherwise to save memory
     * @param imageData Current Image Object
     * @return Returns a boolean if image is visible
     */
    fun isImageVisible(imageData: ImageData): Boolean {
        return (depth - imageData.depth).dp < 500.dp || imageData.resource == null
    }

    /**
     * Converts the image depth in meters to dp and moves it up by half its height so its centered
     * vertically at its depth or returns the depth for the text if image is null
     * @param imageData Current object with image data to process
     * @return Returns the exact vertical position in dp at which to place the image or its text
     */
    fun positionImage(imageData: ImageData): Dp {
        val totalMeters = 10910
        val totalDp = 70000
        val meters = if (imageData.resource == null) {
            imageData.depth.toFloat() / totalMeters * totalDp
        } else {
            (imageData.depth.toFloat() / totalMeters * totalDp) - (imageData.size / 2)
        }
        return meters.dp
    }
}
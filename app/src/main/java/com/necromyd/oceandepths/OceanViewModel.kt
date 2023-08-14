package com.necromyd.oceandepths

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

class OceanViewModel : ViewModel() {
    data class ImageData(val resource: Int, val text: String, val depth: Int, val size: Float)

    var scrollPosition = mutableStateOf(0)
    var isScrollEnabled by mutableStateOf(false)
    var showTextPopUp by mutableStateOf(false)
    var selectedImage: ImageData? by mutableStateOf(null)

    val imageDataList = listOf(
        ImageData(
            R.drawable.cloud,
            "Image at depth 25, long description long description " +
                    "long description long description long description long description long description " +
                    "long description long description long description long description long description ",
            25,
            150f
        ),
        ImageData(R.drawable.cloud, "Image at depth 50", 50, 250f),
        ImageData(R.drawable.cloud, "Image at depth 50", 165, 120f),
        ImageData(R.drawable.cloud, "Image at depth 50", 205, 75f),
        ImageData(R.drawable.cloud, "Image at depth 50", 300, 100f)

    )

    /**
     * This converts scrollPosition's dp to meters that DepthMeter composable's text will show
     * The calculation maps 80000 dp to 10902 meters
     * @return Returns converted dp into meters
     */
    fun getDepth(): Double {
        return scrollPosition.value / 19.259
    }

    /**
     * Function that decides if image should be visible
     * Only images that are within 500dp of the current scrolling position will be loaded
     * Hidden otherwise to save memory
     * @param imageData Current Image Object
     * @return Returns a boolean if image is visible
     */
    fun isImageVisible(imageData: ImageData): Boolean {
        return (getDepth() - imageData.depth).dp < 500.dp
    }

    /**
     * Assigns the current image object to a field so it can be used later and survive config changes
     * @param imageData Current Image Object
     */
    fun selectImage(imageData: ImageData) {
        selectedImage = imageData
        viewModel.showTextPopUp = true
    }

    /**
     * Converts the height of the second box in the OceanDepth composable from dp to meters
     * @param meters Depth at which the current image will be placed
     * @return Returns the dp at which the image will be displayed
     */
    private fun metersToDp(meters: Int): Dp {
        val totalMeters = 10902
        val totalDp = 80000
        return (meters.toFloat() / totalMeters * totalDp).dp
    }

    /**
     * Converts the image depth in meters to dp and moves it up by half its height so its centered
     * vertically at its depth
     * @param imageData Current object with image data to process
     * @return Returns the exact vertical position in dp at which to place the image
     */
    fun positionImage(imageData: ImageData): Dp {
        val place = metersToDp(imageData.depth)
        return place - (imageData.size / 2).dp
    }

}
package com.necromyd.oceandepths


import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

class OceanViewModel(applicationContext: Context) : ViewModel() {
    data class ImageData(
        val resource: Int,
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
    val imageDataList = listOf<ImageData>(
        ImageData(
            R.drawable.sunlit_zone,
            applicationContext.getString(R.string.sunlit_zone),
            applicationContext.getString(R.string.sunlit_zone_text),
            25,
            60f
        ),
        ImageData(
            R.drawable.twilight_zone,
            applicationContext.getString(R.string.twilight_zone),
            applicationContext.getString(R.string.twilight_zone_text),
            200,
            60f
        ),
        ImageData(
            R.drawable.midnight_zone,
            applicationContext.getString(R.string.midnight_zone),
            applicationContext.getString(R.string.midnight_zone_text),
            1000,
            60f
        ),
        ImageData(
            R.drawable.abyssal_zone,
            applicationContext.getString(R.string.abyssal_zone),
            applicationContext.getString(R.string.abyssal_zone_text),
            4000,
            60f
        ),
        ImageData(
            R.drawable.hadal_zone,
            applicationContext.getString(R.string.hadal_zone),
            applicationContext.getString(R.string.hadal_zone_text),
            6000,
            60f
        ),
    )

    /**
     * Function that decides if image should be visible
     * Only images that are within 500dp of the current scrolling position will be loaded
     * Hidden otherwise to save memory
     * @param imageData Current Image Object
     * @return Returns a boolean if image is visible
     */
    fun isImageVisible(imageData: OceanViewModel.ImageData): Boolean {
        return (depth - imageData.depth).dp < 500.dp
    }

    /**
     * Converts the image depth in meters to dp and moves it up by half its height so its centered
     * vertically at its depth
     * @param imageData Current object with image data to process
     * @return Returns the exact vertical position in dp at which to place the image
     */
    fun positionImage(imageData: OceanViewModel.ImageData): Dp {
        val totalMeters = 10910
        val totalDp = 70000
        val meters = (imageData.depth.toFloat() / totalMeters * totalDp) - (imageData.size / 2)
        return meters.dp
    }
}
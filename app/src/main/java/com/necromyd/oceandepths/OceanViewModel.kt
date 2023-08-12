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
        ImageData(R.drawable.cloud, "Image at depth 25, long description long description " +
                "long description long description long description long description long description " +
                "long description long description long description long description long description ", 45, 150f),
        ImageData(R.drawable.cloud, "Image at depth 50", 450, 150f)

    )

    fun getDepth(): Double{
        return scrollPosition.value / 19.259
    }

    fun isImageVisible(imageData: ImageData): Boolean {
        return (getDepth() - imageData.depth).dp < 500.dp
    }

    fun selectImage(imageData: ImageData) {
        selectedImage = imageData
        viewModel.showTextPopUp = true
    }

    fun metersToDp(meters: Int): Dp {
        val totalMeters = 10902
        val totalDp = 80000
        return (meters.toFloat() / totalMeters * totalDp).dp
    }

    fun positionImage(imageData: ImageData): Dp{
        var place = metersToDp(imageData.depth)
        var offset = place - (imageData.size/2).dp
        return offset
    }

}
package com.necromyd.oceandepths

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

class OceanViewModel : ViewModel() {
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

    val imageDataList = listOf(
        ImageData(
            R.drawable.cloud, "The Sunlit Zone",
            "The sunlit ocean zone, also known as the euphotic or epipelagic zone, is the uppermost layer" +
                    " of the ocean which sunlight penetrates and supports photosynthesis. This zone " +
                    "typically extends from the ocean's surface down to a depth of around 200 meters" +
                    ". Within the euphotic zone, marine plants, such as phytoplankton, " +
                    "use sunlight to produce energy through photosynthesis, which forms the " +
                    "foundation of the ocean's food chain. This zone is rich in biodiversity and is" +
                    " home to a wide variety of marine organisms, from small zooplankton to larger" +
                    " fish and marine mammals.",
            25, 150f
        ),
        ImageData(
            R.drawable.cloud, "The Twilight Zone", "The twilight zone, also referred to as the " +
                    "mesopelagic zone, is the ocean layer that begins from the edge of the euphotic" +
                    " zone (where sunlight diminishes)." +
                    " In this zone, sunlight is still present but extremely faint and appears as a dim twilight, " +
                    "hence the name. The twilight zone is characterized by a significant decrease" +
                    " in light intensity and a decrease in temperature with increasing depth." +
                    " Many marine organisms in this zone have developed adaptations to low light" +
                    " conditions, including bioluminescence, the ability to produce their own light." +
                    " Depending on the water clarity and the angle at which light penetrates, this zone" +
                    " tends to have a varying depth to which light may be seen in. Up to 1000m in the" +
                    " absolute best case scenario.",
            200, 150f
        ),
        ImageData(
            R.drawable.cloud,
            "The Midnight Zone",
            "The midnight zone, also known as the bathypelagic zone, is the oceanic layer that extends from the bottom of the twilight zone around 1000 meters deep. This zone is characterized by complete darkness, cold temperatures, and strong pressure. The midnight zone is one of the most mysterious and least explored regions of the ocean. Many of the organisms that inhabit this zone have developed unique adaptations to survive in the harsh conditions, such as pressure-resistant body structures and specialized feeding strategies.",
            1000,
            150f
        ),
        ImageData(
            R.drawable.cloud,
            "The Abyssal Zone",
            "The abyssal zone, often referred to as the abyssopelagic zone, is the deepest and most remote layer of the ocean. It begins at the edge of the midnight zone, around 4000 meters below the ocean's surface, and extends down to the ocean floor or 6000m. This zone is characterized by extremely high pressure, complete darkness, and near-freezing temperatures. The abyssal zone is home to a variety of unique and often bizarre organisms that have evolved to survive in these extreme conditions. It also contains hydrothermal vents, where superheated water rich in minerals emerges from the seafloor, supporting specialized ecosystems.",
            4000,
            150f
        ),
        ImageData(
            R.drawable.cloud,
            "The Hadal Zone",
            "The hadal zone is the oceanic zone that encompasses the deepest ocean trenches, ranging from around 6000 meters to the bottom of the ocean's deepest points. This zone is characterized by extreme pressure, complete darkness, and cold temperatures, similar to the abyssal zone. However, what sets the hadal zone apart is its unique geological features, including deep-sea trenches and subduction zones. Despite the challenging conditions, life has been discovered even in these depths, including specialized organisms that have adapted to thrive in the harsh environment.",
            6000,
            150f
        )

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
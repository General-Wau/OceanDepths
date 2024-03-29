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
            R.drawable.img_pool,
            applicationContext.getString(R.string.deepest_pool),
            applicationContext.getString(R.string.deepest_pool_text),
            60,
            200f
        ),
        ImageData(
            R.drawable.img_eurotunnel,
            applicationContext.getString(R.string.eurotunnel),
            applicationContext.getString(R.string.eurotunnel_text),
            115,
            200f
        ),
        ImageData(
            R.drawable.img_coral,
            applicationContext.getString(R.string.coral_reef),
            applicationContext.getString(R.string.coral_reef_text),
            145,
            200f
        ),
        ImageData(
            R.drawable.img_seal,
            applicationContext.getString(R.string.northern_fur_seal),
            applicationContext.getString(R.string.northern_fur_seal_text),
            175,
            150f
        ),
        ImageData(
            R.drawable.twilight_zone,
            applicationContext.getString(R.string.twilight_zone),
            applicationContext.getString(R.string.twilight_zone_text),
            200,
            60f
        ),
        ImageData(
            R.drawable.img_bottlenose,
            applicationContext.getString(R.string.bottlenose),
            applicationContext.getString(R.string.bottlenose_text),
            250,
            150f
        ),
        ImageData(
            R.drawable.img_sinkhole,
            applicationContext.getString(R.string.dragon_hole),
            applicationContext.getString(R.string.dragon_hole_text),
            300,
            200f
        ),
        ImageData(
            R.drawable.img_scuba_dive,
            applicationContext.getString(R.string.scuba_dive),
            applicationContext.getString(R.string.scuba_dive_text),
            332,
            150f
        ),
        ImageData(
            R.drawable.img_spider_crab,
            applicationContext.getString(R.string.spider_crab),
            applicationContext.getString(R.string.spider_crab_text),
            600,
            200f
        ),
        ImageData(
            R.drawable.img_burj,
            applicationContext.getString(R.string.burj),
            applicationContext.getString(R.string.burj_text),
            828,
            200f
        ),
        ImageData(
            R.drawable.img_vamp_squid,
            applicationContext.getString(R.string.vampire_squid),
            applicationContext.getString(R.string.vampire_squid_text),
            900,
            200f
        ),
        ImageData(
            R.drawable.midnight_zone,
            applicationContext.getString(R.string.midnight_zone),
            applicationContext.getString(R.string.midnight_zone_text),
            1000,
            60f
        ),
        ImageData(
            R.drawable.img_greatwhite,
            applicationContext.getString(R.string.great_white),
            applicationContext.getString(R.string.great_white_text),
            1170,
            250f
        ),
        ImageData(
            R.drawable.img_openpit,
            applicationContext.getString(R.string.open_pit_mine),
            applicationContext.getString(R.string.open_pit_mine_text),
            1210,
            250f
        ),
        ImageData(
            R.drawable.img_lake,
            applicationContext.getString(R.string.lake),
            applicationContext.getString(R.string.lake_text),
            1642,
            250f
        ),
        ImageData(
            R.drawable.img_squid,
            applicationContext.getString(R.string.squid),
            applicationContext.getString(R.string.squid_text),
            2000,
            350f
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
        ImageData(
            R.drawable.img_volcano,
            applicationContext.getString(R.string.volcano),
            applicationContext.getString(R.string.volcano_text),
            6893,
            250f
        ),
        ImageData(
            R.drawable.img_everest,
            applicationContext.getString(R.string.everest),
            applicationContext.getString(R.string.everest_text),
            8848,
            250f
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
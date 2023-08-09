package com.necromyd.oceandepths

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class OceanViewModel : ViewModel() {
    var scrollPosition = mutableStateOf(0)
    var isScrollEnabled = mutableStateOf(false)

    fun getDepth(): Double{
        return scrollPosition.value / 19.259
    }

}
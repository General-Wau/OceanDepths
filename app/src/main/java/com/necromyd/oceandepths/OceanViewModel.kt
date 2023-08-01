package com.necromyd.oceandepths

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class OceanViewModel : ViewModel() {
//    var depth = mutableStateOf(0)
    var scrollPosition = mutableStateOf(0)

    fun getDepth(): Double{
        return scrollPosition.value / 19.259
    }
}
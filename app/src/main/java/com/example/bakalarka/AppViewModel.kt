package com.example.bakalarka

import android.content.res.Resources
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {

    var screenWidth = 0
        private set

    var screenHeight = 0
        private set

    var density = 1f
        private set

    fun setScreenInfo(width: Int, height: Int, density: Float) {
        screenWidth = width
        screenHeight = height
        this.density = density
    }
    fun getScreenHeightPX(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }


    fun pxToDp(px: Int): Float = px / density

}

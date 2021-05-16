package com.education.android.myquizappmain

import android.annotation.SuppressLint
import com.stephentuso.welcome.WelcomeConfiguration

class WelcomeActivity : com.stephentuso.welcome.WelcomeActivity() {

    @SuppressLint("ResourceAsColor")
    override fun configuration(): WelcomeConfiguration? {
        return WelcomeConfiguration.Builder(this)
            .swipeToDismiss(true)
            .bottomLayout(WelcomeConfiguration.BottomLayout.INDICATOR_ONLY)
            .canSkip(false)
            .build()
    }
}
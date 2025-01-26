package com.kevinschildhorn.fantasypuppybowl

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val rowSize: Int
        get() = 4
}

actual fun getPlatform(): Platform = AndroidPlatform()
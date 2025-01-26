package com.kevinschildhorn.fantasypuppybowl

interface Platform {
    val name: String
    val rowSize: Int
}

expect fun getPlatform(): Platform
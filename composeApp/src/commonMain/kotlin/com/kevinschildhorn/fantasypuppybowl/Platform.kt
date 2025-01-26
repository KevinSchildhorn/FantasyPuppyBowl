package com.kevinschildhorn.fantasypuppybowl

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
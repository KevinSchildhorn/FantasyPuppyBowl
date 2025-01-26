package com.kevinschildhorn.fantasypuppybowl

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val rowSize: Int
        get() = 8
}

actual fun getPlatform(): Platform = JVMPlatform()
package com.zerodev.wa.utils

object Secured {
    init {
        System.loadLibrary("wa")
    }

    external fun getDirImages(): String
    external fun getDirVideos(): String
}

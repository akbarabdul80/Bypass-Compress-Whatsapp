package com.zerodev.wa.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.zerodev.wa.R
import java.io.File

fun loadImage(imageView: ImageView, file: File) {
    Picasso.get()
        .load(file)
        .placeholder(R.drawable.list_placeholder)
        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
        .into(imageView)
}
package com.example.camera

import android.graphics.Bitmap
import android.net.Uri

data class InternalStoragePhoto(
    val name: String,
    val bmp: Bitmap
)
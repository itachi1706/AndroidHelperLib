package com.itachi1706.helperlib.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.helpers in Helper Library
 */
@Suppress("unused")
object BitmapUtil {
    @JvmStatic
    @RequiresApi(VERSION_CODES.LOLLIPOP)
    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    @JvmStatic
    private fun getBitmap(vectorDrawable: VectorDrawableCompat): Bitmap {
        val bitmap = createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    @JvmStatic
    fun getBitmap(context: Context, @DrawableRes drawableResId: Int): Bitmap? {
        return when (val drawable = ContextCompat.getDrawable(context, drawableResId)) {
            is BitmapDrawable -> drawable.bitmap
            is VectorDrawableCompat -> getBitmap(drawable)
            else -> {
                if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && drawable is VectorDrawable) getBitmap(
                    drawable
                )
                else throw IllegalArgumentException("Unsupported drawable type")
            }
        }
    }
}
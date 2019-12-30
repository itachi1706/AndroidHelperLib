package com.itachi1706.helperlib.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.helpers in Helper Library
 */
object BitmapUtil {
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    @JvmStatic
    private fun getBitmap(vectorDrawable: VectorDrawableCompat): Bitmap? {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    @JvmStatic
    fun getBitmap(context: Context, @DrawableRes drawableResId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableResId)
        return if (drawable is BitmapDrawable) drawable.bitmap
        else if (drawable is VectorDrawableCompat) getBitmap(
            drawable
        )
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && drawable is VectorDrawable) getBitmap(
            drawable
        )
        else throw IllegalArgumentException("Unsupported drawable type")
    }
}
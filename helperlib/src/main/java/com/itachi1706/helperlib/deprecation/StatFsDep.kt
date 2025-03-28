package com.itachi1706.helperlib.deprecation

import android.os.StatFs

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.deprecation in Helper Library
 */
@Suppress("unused")
@Deprecated("Use StatFs instead", ReplaceWith("StatFs"))
object StatFsDep {
    @JvmStatic
    fun getBlockSize(statFs: StatFs): Long {
        return statFs.blockSizeLong
    }

    @JvmStatic
    fun getBlockCount(statFs: StatFs): Long {
        return statFs.blockCountLong
    }

    @JvmStatic
    fun getAvailableBlocks(statFs: StatFs): Long {
        return statFs.availableBlocksLong
    }
}
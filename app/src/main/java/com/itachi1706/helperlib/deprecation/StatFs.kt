package com.itachi1706.helperlib.deprecation

import android.os.Build
import android.os.StatFs

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.deprecation in Helper Library
 */
@Suppress("DEPRECATION")
object StatFs {
    @JvmStatic
    fun getBlockSize(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) statFs.blockSizeLong
        else statFs.blockSize.toLong()
    }

    @JvmStatic
    fun getBlockCount(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) statFs.blockCountLong
        else statFs.blockCount.toLong()
    }

    @JvmStatic
    fun getAvailableBlocks(statFs: StatFs): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) statFs.availableBlocksLong
        else statFs.availableBlocks.toLong()
    }
}
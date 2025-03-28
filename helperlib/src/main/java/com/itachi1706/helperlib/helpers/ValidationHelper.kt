package com.itachi1706.helperlib.helpers

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.util.Log
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * Created by Kenneth on 30/12/2019.
 * for com.itachi1706.helperlib.helpers in Helper Library
 */
@Suppress("unused")
object ValidationHelper {

    const val SIDELOAD = 0
    const val GOOGLE_PLAY = 1 // com.android.vending, com.google.android.feedback
    const val AMAZON = 2 // com.amazon.venezia
    const val HUAWEI = 3 // com.huawei.appmarket

    private val playstoreList: List<String> =
        ArrayList(listOf("com.android.vending", "com.google.android.feedback"))
    private val amazonList: List<String> = ArrayList(listOf("com.amazon.venezia"))
    private val huaweiList: List<String> = ArrayList(listOf("com.huawei.appmarket"))

    @JvmStatic
    fun checkNotSideloaded(context: Context): Boolean {
        val mergedList: MutableList<String> = ArrayList()
        mergedList.addAll(playstoreList)
        mergedList.addAll(amazonList)
        mergedList.addAll(huaweiList)
        val installer = getInstallLocation(context, context.packageName)
        return installer != null && mergedList.contains(installer)
    }

    @JvmStatic
    fun checkSideloaded(context: Context): Boolean {
        return !checkNotSideloaded(context)
    }

    @JvmStatic
    fun getInstallLocation(context: Context): String? {
        return getInstallLocation(context, context.packageName)
    }

    @JvmStatic
    fun getInstallLocation(context: Context, packageName: String?): String? {
        if (packageName == null) return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val installInfo = context.packageManager.getInstallSourceInfo(packageName)
            installInfo.installingPackageName
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getInstallerPackageName(packageName)
        }
    }

    @JvmStatic
    fun checkInstallLocation(context: Context): Int {
        return checkInstallLocation(context, context.packageName)
    }

    @JvmStatic
    fun checkInstallLocation(context: Context, packageName: String?): Int {
        val installer = getInstallLocation(context, packageName) ?: return SIDELOAD
        if (playstoreList.contains(installer)) return GOOGLE_PLAY
        else if (huaweiList.contains(installer)) return HUAWEI
        return if (amazonList.contains(installer)) AMAZON else SIDELOAD
    }

    // Signature Validation
    @JvmStatic
    fun getSignatureForValidation(context: Context): String {
        val pm = context.packageManager
        val signatures: Array<Signature>
        return try {
            signatures = getSignatures(pm, context)
            getSignatureString(signatures[0]).trim { it <= ' ' }
        } catch (e: RuntimeException) {
            Log.e("ValidationHelper", "Failed to get package info. Signature cannot be validated")
            return "error"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.e("ValidationHelper", "Failed to get package info. Signature cannot be validated")
            "error"
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.e(
                "ValidationHelper",
                "Algorithm not recognized on this Android Version, signature cannot be validated"
            )
            "error"
        }
    }

    @JvmStatic
    @Suppress("DEPRECATION")
    @Throws(RuntimeException::class)
    private fun getSignatures(pm: PackageManager, context: Context): Array<Signature> {
        val pInfo: PackageInfo = pm.getPackageInfo(
            context.packageName,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PackageManager.GET_SIGNING_CERTIFICATES
            else PackageManager.GET_SIGNATURES
        )
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pInfo.signingInfo!!.apkContentsSigners else pInfo.signatures!!
    }

    @JvmStatic
    @Throws(CertificateException::class)
    fun getCert(cert: ByteArray?): X509Certificate {
        val inputStream = ByteArrayInputStream(cert)
        val cf = CertificateFactory.getInstance("X509")
        return cf.generateCertificate(inputStream) as X509Certificate
    }

    @JvmStatic
    @Throws(NoSuchAlgorithmException::class)
    fun getSignatureString(sig: Signature): String {
        val cert = sig.toByteArray()
        return try {
            bytesToHex(MessageDigest.getInstance("SHA1").digest(getCert(cert).encoded))
        } catch (e: CertificateException) {
            Log.e("Signature", "Cannot Create Signature, Falling back")
            Log.e("Signature", "Error: " + e.localizedMessage)
            val md = MessageDigest.getInstance("SHA1")
            md.update(sig.toByteArray())
            bytesToHex(md.digest())
        }
    }

    private val hexArray = "0123456789ABCDEF".toCharArray()

    @JvmStatic
    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v: Int = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        val sb = StringBuilder()
        for (i in hexChars.indices) {
            if (i % 2 == 0 && i != 0) sb.append(":")
            sb.append(hexChars[i])
        }
        return sb.toString()
    }
}
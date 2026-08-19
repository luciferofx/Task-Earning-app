package com.example.data.security

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import com.example.data.model.SecurityAuditResult
import java.io.File

object SecurityChecker {

    fun performFullSecurityAudit(context: Context): SecurityAuditResult {
        val isRooted = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
        val isVpn = checkVpnActive(context)
        val deviceId = getDeviceFingerprint(context)

        var integrityScore = 100
        if (isRooted) integrityScore -= 60
        if (isVpn) integrityScore -= 30

        val isSafe = integrityScore >= 70
        val summary = when {
            isRooted && isVpn -> "Critical Risk: Root access and VPN detected. Payouts locked."
            isRooted -> "High Risk: Rooted environment detected. Task rewards restricted."
            isVpn -> "Warning: VPN/Proxy active. Disable VPN to submit high-value tasks."
            else -> "Secure & Verified: Device integrity passed. All rewards available."
        }

        return SecurityAuditResult(
            isRooted = isRooted,
            isVpnOrProxy = isVpn,
            deviceId = deviceId,
            isSafeToEarn = isSafe,
            statusSummary = summary,
            integrityScore = integrityScore.coerceIn(0, 100)
        )
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        return paths.any { path ->
            try {
                File(path).exists()
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val inStream = process.inputStream.bufferedReader()
            inStream.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    fun checkVpnActive(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val activeNetwork = cm?.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(activeNetwork) ?: return false
            caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } catch (e: Exception) {
            false
        }
    }

    fun getDeviceFingerprint(context: Context): String {
        return try {
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val model = Build.MODEL.replace(" ", "-")
            val brand = Build.MANUFACTURER.uppercase()
            "${brand}-${model}-${(androidId ?: "UNKNOWN").takeLast(6)}"
        } catch (e: Exception) {
            "DEV-${Build.BOARD}-9481"
        }
    }
}

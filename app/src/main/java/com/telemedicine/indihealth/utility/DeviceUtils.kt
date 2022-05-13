package com.telemedicine.indihealth.utility

import android.content.Context
import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object DeviceUtils {
    fun isDeviceRooted(context: Context?): Boolean {
        return isRooted1 || isRooted2
    }

    private val isRooted1: Boolean
        get() {
            val file = File("/system/app/Superuser.apk")
            return file.exists()
        }

    // try executing commands
    private val isRooted2: Boolean
        get() = (canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su")
                || canExecuteCommand("which su"))

    private fun canExecuteCommand(command: String): Boolean {
        return try {
            Runtime.getRuntime().exec(command)
            true
        } catch (e: Exception) {
            false
        }
    }
}

object RootUtil {
    val isDeviceRooted: Boolean
        get() = checkRootMethod1() || checkRootMethod2() || checkRootMethod3()

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
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val `in` = BufferedReader(InputStreamReader(process.inputStream))
            if (`in`.readLine() != null) true else false
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }
}
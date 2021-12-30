package com.erivera.apps.topcharts

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import javax.inject.Inject

class DeviceManagerImpl @Inject constructor(val context: Context) : DeviceManager {

    private val deviceInfo = DeviceManager.DeviceInfo(0, 0, 0, 0, 0)

    override fun getFullDeviceHeight(): Int {
        return deviceInfo.fullScreenHeight
    }

    override fun getFullDeviceWidth(): Int {
        return deviceInfo.fullScreenWidth
    }

    override fun getDeviceInfo(): DeviceManager.DeviceInfo {
        return deviceInfo
    }

    override fun setDefaultWidthHeight(windowManager: WindowManager, statusBarHeight: Int) {
        val displayMetrics = DisplayMetrics()
        val point = Point()
        val display = windowManager.defaultDisplay
        display.getRealSize(point)
        deviceInfo.fullScreenWidth = point.x
        deviceInfo.fullScreenHeight = point.y

        windowManager.defaultDisplay.getMetrics(displayMetrics)
        deviceInfo.useableHeight = displayMetrics.heightPixels
        deviceInfo.useableWidth = displayMetrics.widthPixels
        deviceInfo.statusBarHeight = statusBarHeight
        Log.d(
            DeviceManagerImpl::class.java.name, "setDefaultWidthHeight" +
                    ":fullscreenHeight ${deviceInfo.fullScreenHeight}:fullScreenWidth ${deviceInfo.fullScreenWidth}" +
                    ":useableHeight ${deviceInfo.useableHeight}:useableWidth ${deviceInfo.useableWidth}:statusBarHeight${deviceInfo.statusBarHeight}"
        )
    }
}
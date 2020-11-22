package com.erivera.apps.topcharts

import android.view.WindowManager

interface DeviceManager {
    fun getFullDeviceHeight() : Int

    fun getFullDeviceWidth() : Int

    fun setDefaultWidthHeight(windowManager: WindowManager, statusBarHeight: Int)

    fun getDeviceInfo() : DeviceInfo

    class DeviceInfo(var fullScreenHeight: Int, var fullScreenWidth: Int, var useableHeight: Int, var useableWidth: Int, var statusBarHeight: Int)
}
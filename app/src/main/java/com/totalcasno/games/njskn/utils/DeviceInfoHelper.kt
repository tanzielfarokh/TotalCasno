package com.totalcasno.games.njskn.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import java.util.Locale

object DeviceInfoHelper {
    
    fun getOperatingSystem(): String {
        return "Android ${Build.VERSION.RELEASE}"
    }
    
    fun getLanguage(): String {
        return Locale.getDefault().language
    }
    
    fun getRegion(): String {
        return Locale.getDefault().country
    }
    
    fun getDeviceModel(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }
    
    fun getBatteryStatus(context: Context): String {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
            context.registerReceiver(null, filter)
        }
        
        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        
        return when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "NotCharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            else -> "Unknown"
        }
    }
    
    fun getBatteryLevel(context: Context): String {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
            context.registerReceiver(null, filter)
        }
        
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        
        if (level == -1 || scale == -1) {
            return "0"
        }
        
        val batteryPct = level / scale.toFloat()
        
        return if (batteryPct == 1.0f) {
            "1"
        } else {
            batteryPct.toString()
        }
    }
    
    fun buildRequestLink(baseLink: String, context: Context): String {
        val os = getOperatingSystem()
        val language = getLanguage()
        val region = getRegion()
        val deviceModel = getDeviceModel()
        val batteryStatus = getBatteryStatus(context)
        val batteryLevel = getBatteryLevel(context)
        
        return baseLink
            .replace("OS_SYSTEM", os)
            .replace("LANGUAGE_SYSTEM", language)
            .replace("REGION_DEVICE", region)
            .replace("DEVICE_MODEL", deviceModel)
            .replace("BATTERY_STATUS", batteryStatus)
            .replace("BATTERY_LEVEL", batteryLevel)
    }
}


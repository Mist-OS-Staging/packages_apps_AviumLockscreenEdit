/*
 * Copyright (C) 2025 The AviumUI Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.avium.lockscreenedit.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.os.UserHandle
import android.util.Log

object SystemSettingsManager {

    private const val TAG = "MIST_LOCKSCREEN"

    // These keys MUST match exactly in all files
    private const val KEY_ENABLED = "custom_lockscreen_enable"
    private const val KEY_TYPE = "custom_lockscreen_type"
    private const val KEY_COLOR = "custom_lockscreen_color"
    private const val KEY_HOUR_COLOR = "custom_lockscreen_hour_color"
    private const val KEY_MINUTE_COLOR = "custom_lockscreen_minute_color"
    
    private const val ACTION_SETTINGS_CHANGED = "org.mist.systemui.lockscreen.SETTINGS_CHANGED"

    fun setEnabled(context: Context, enabled: Boolean) {
        Log.d(TAG, "Setting lockscreen enabled to: $enabled")
        Settings.System.putIntForUser(
            context.contentResolver,
            KEY_ENABLED,
            if (enabled) 1 else 0,
            UserHandle.USER_CURRENT
        )
    }

    fun setClockType(context: Context, type: Int) {
        Log.d(TAG, "Setting clock type to: $type")
        Settings.System.putIntForUser(
            context.contentResolver,
            KEY_TYPE,
            type,
            UserHandle.USER_CURRENT
        )
    }

    fun setClockColor(context: Context, color: String) {
        Log.d(TAG, "Setting clock color to: $color")
        Settings.System.putStringForUser(
            context.contentResolver,
            KEY_COLOR,
            color,
            UserHandle.USER_CURRENT
        )
    }

    fun setHourColor(context: Context, color: String) {
        Log.d(TAG, "Setting hour color to: $color")
        Settings.System.putStringForUser(
            context.contentResolver,
            KEY_HOUR_COLOR,
            color,
            UserHandle.USER_CURRENT
        )
    }

    fun setMinuteColor(context: Context, color: String) {
        Log.d(TAG, "Setting minute color to: $color")
        Settings.System.putStringForUser(
            context.contentResolver,
            KEY_MINUTE_COLOR,
            color,
            UserHandle.USER_CURRENT
        )
    }

    fun sendSettingsChangedBroadcast(context: Context) {
        Log.d(TAG, "Sending settings changed broadcast")
        val intent = Intent(ACTION_SETTINGS_CHANGED)
        intent.flags = Intent.FLAG_RECEIVER_INCLUDE_BACKGROUND
        context.sendBroadcast(intent)
    }

    fun applySettings(context: Context, styleId: Int) {
        Log.d(TAG, "Applying settings for styleId: $styleId")
        setEnabled(context, true)
        setClockType(context, styleId)
        setHourColor(context, "FFFFFF")
        setMinuteColor(context, "FFFFFF")
        setClockColor(context, "FFFFFF")
        sendSettingsChangedBroadcast(context)
    }
    
    fun applyCustomColors(
        context: Context, 
        styleId: Int, 
        hourColor: String, 
        minuteColor: String, 
        isBlurEnabled: Boolean
    ) {
        Log.d(TAG, "Applying custom colors - styleId: $styleId, hourColor: $hourColor, minuteColor: $minuteColor, blur: $isBlurEnabled")
        setEnabled(context, true)
        setClockType(context, styleId)
        
        if (isBlurEnabled) {
            setClockColor(context, "blur")
            setHourColor(context, hourColor)
            setMinuteColor(context, minuteColor)
        } else {
            setHourColor(context, hourColor)
            setMinuteColor(context, minuteColor)
            setClockColor(context, "FFFFFF")
        }
        
        sendSettingsChangedBroadcast(context)
    }

    fun isEnabled(context: Context): Boolean {
        return Settings.System.getIntForUser(
            context.contentResolver,
            KEY_ENABLED,
            0,
            UserHandle.USER_CURRENT
        ) == 1
    }

    fun getClockType(context: Context): Int {
        return Settings.System.getIntForUser(
            context.contentResolver,
            KEY_TYPE,
            0,
            UserHandle.USER_CURRENT
        )
    }

    fun getHourColor(context: Context): String {
        return Settings.System.getStringForUser(
            context.contentResolver,
            KEY_HOUR_COLOR,
            UserHandle.USER_CURRENT
        ) ?: "FFFFFF"
    }

    fun getMinuteColor(context: Context): String {
        return Settings.System.getStringForUser(
            context.contentResolver,
            KEY_MINUTE_COLOR,
            UserHandle.USER_CURRENT
        ) ?: "FFFFFF"
    }
}

package com.liangguo.preference.utils

import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.liangguo.easyingcontext.EasyingContext.context


/**
 * @author ldh
 * 时间: 2022/1/23 14:06
 * 邮箱: 2637614077@qq.com
 */
object PreferenceUtil {

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val mResources = context.resources

    fun getBoolean(@StringRes keyId: Int, defaultValue: Boolean = false) =
        getBoolean(mResources.getString(keyId), defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean = false) =
        sharedPreferences.getBoolean(key, defaultValue)

    fun getFloat(@StringRes keyId: Int, defaultValue: Float) =
        sharedPreferences.getFloat(mResources.getString(keyId), defaultValue)

    fun getFloat(key: String, defaultValue: Float) =
        sharedPreferences.getFloat(key, defaultValue)

    fun getString(@StringRes keyId: Int, defaultValue: String) =
        sharedPreferences.getString(mResources.getString(keyId), defaultValue) ?: defaultValue

    fun getString(key: String, defaultValue: String) =
        sharedPreferences.getString(key, defaultValue) ?: defaultValue


    fun getString(@StringRes keyId: Int) =
        sharedPreferences.getString(mResources.getString(keyId), null)

    fun getString(key: String) =
        sharedPreferences.getString(key, null)


    fun registerOnSharedPreferenceChangedListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener
    ) = sharedPreferences.registerOnSharedPreferenceChangeListener(listener)


    fun unregisterOnSharedPreferenceChangedListener(
        changeListener: SharedPreferences.OnSharedPreferenceChangeListener
    ) = sharedPreferences.unregisterOnSharedPreferenceChangeListener(changeListener)

    fun setBoolean(@StringRes keyId: Int, value: Boolean) {
        setBoolean(mResources.getString(keyId), value)
    }

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
            commit()
        }
    }

    fun setFloat(@StringRes keyId: Int, value: Float) = setFloat(mResources.getString(keyId), value)

    fun setFloat(key: String, value: Float) =
        sharedPreferences.edit {
            putFloat(key, value)
            commit()
        }

    fun setString(@StringRes keyId: Int, value: String?) = setString(mResources.getString(keyId), value)

    fun setString(key: String, value: String?) =
        sharedPreferences.edit {
            putString(key, value)
            commit()
        }

}


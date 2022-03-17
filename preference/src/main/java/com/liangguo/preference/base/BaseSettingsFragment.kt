package com.liangguo.preference.base

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.RecyclerView
import com.liangguo.preference.dialog.MyListPreferenceDialogFragmentCompat
import com.liangguo.preference.utils.PreferenceUtil


/**
 * @author ldh
 * 时间: 2022/1/23 12:16
 * 邮箱: 2637614077@qq.com
 */
abstract class BaseSettingsFragment : PreferenceFragmentCompat() {

    val sharedPreferences: SharedPreferences
        get() = PreferenceUtil.sharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCheckSettingState()
    }

    override fun onCreateRecyclerView(
        inflater: LayoutInflater,
        parent: ViewGroup,
        savedInstanceState: Bundle?
    ): RecyclerView {
        return super.onCreateRecyclerView(inflater, parent, savedInstanceState).apply {
            //这里把RecyclerView的OverScroll阴影全部进制掉，因为我们用的是SmartRefreshLayout
            overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    /**
     * 这个方法主要是检测设置项的状态，比如有些设置项需要动态设置enable
     * 这个方法会在父类的[onViewCreated]中被调用
     */
    protected open fun onCheckSettingState() {}

    @SuppressLint("RestrictedApi")
    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (callbackFragment is OnPreferenceDisplayDialogCallback) {
            (callbackFragment as OnPreferenceDisplayDialogCallback?)!!.onPreferenceDisplayDialog(
                this,
                preference
            )
            return
        }

        if (this.activity is OnPreferenceDisplayDialogCallback) {
            (this.activity as OnPreferenceDisplayDialogCallback?)!!.onPreferenceDisplayDialog(
                this,
                preference
            )
            return
        }

        if (requireFragmentManager().findFragmentByTag("androidx.preference.PreferenceFragment.DIALOG") == null) {
            val dialogFragment = onCreatePreferenceDialog(preference)
            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.show(
                    this.requireFragmentManager(),
                    "androidx.preference.PreferenceFragment.DIALOG"
                )
                return
            }
        }
        super.onDisplayPreferenceDialog(preference)
    }

    open fun onCreatePreferenceDialog(preference: Preference): DialogFragment? {
        if (preference is ListPreference) {
            return MyListPreferenceDialogFragmentCompat.newInstance(preference.key)
        } else if (preference is BasePreferenceDialogFragment) {
            return BasePreferenceDialogFragment.newInstance(preference.key)
        }
        return null
    }

}
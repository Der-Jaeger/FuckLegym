package com.liangguo.preference.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.preference.ListPreferenceDialogFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liangguo.preference.R
import com.liangguo.preference.extensions.materialDialog


/**
 * @author ldh
 * 时间: 2022/1/28 11:30
 * 邮箱: 2637614077@qq.com
 */
class SingleChoiceListPreferenceDialogFragmentCompat : ListPreferenceDialogFragmentCompat(),
    DialogInterface.OnClickListener {

    val entries = mutableListOf<CharSequence>()
    val entryValues = mutableListOf<CharSequence>()

    val key: String by lazy {
        tag
            ?: throw IllegalArgumentException("没有找到PreferenceKey，请在启动这个Fragment时设置tag为preferenceKey")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return materialDialog()
            .setPositiveButton(R.string.confirm) { _, _ ->
                dismiss()
            }
            .setItems(entryValues.toTypedArray(), this)
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    private fun refreshBlacklistData() {
        val dialog = dialog as MaterialAlertDialogBuilder?
        dialog?.setItems(entries.toTypedArray(), null)
    }

}
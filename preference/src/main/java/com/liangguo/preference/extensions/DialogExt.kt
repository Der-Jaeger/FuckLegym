package com.liangguo.preference.extensions

import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liangguo.preference.R


/**
 * @author ldh
 * 时间: 2022/1/28 11:35
 * 邮箱: 2637614077@qq.com
 */
fun DialogFragment.materialDialog(title: Int = -1): MaterialAlertDialogBuilder {
    return MaterialAlertDialogBuilder(
        requireContext(),
        R.style.MaterialAlertDialog_Material3
    ).apply {
        if (title != -1) {
            setTitle(title)
        }
    }
}


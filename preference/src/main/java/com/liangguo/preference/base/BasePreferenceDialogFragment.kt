package com.liangguo.preference.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.DialogPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liangguo.preference.R

open class BasePreferenceDialogFragment : DialogFragment(), DialogInterface.OnClickListener {

    private var mWhichButtonClicked = 0

    var preference: DialogPreference? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rawFragment: Fragment = this.targetFragment!!
        check(rawFragment is DialogPreference.TargetFragment) { "Target fragment 必须实现 TargetFragment 接口" }
        val fragment = rawFragment as DialogPreference.TargetFragment
        val key: String = requireArguments().getString(ARG_KEY)!!
        preference = fragment.findPreference(key)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context: FragmentActivity = requireActivity()
        val builder = MaterialAlertDialogBuilder(
            context,
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
            .setTitle(preference!!.dialogTitle)
            .setIcon(preference!!.dialogIcon)
            .setMessage(preference!!.dialogMessage)
            .setPositiveButton(preference!!.positiveButtonText, this)
            .setNegativeButton(preference!!.negativeButtonText, this)
        onPrepareDialogBuilder(builder)
        val dialog = builder.create()
        if (needInputMethod()) {
            requestInputMethod(dialog)
        }
        return dialog
    }

    protected open fun onPrepareDialogBuilder(builder: MaterialAlertDialogBuilder?) {}
    protected open fun needInputMethod(): Boolean {
        return false
    }

    private fun requestInputMethod(dialog: Dialog) {
        val window = dialog.window
        window!!.setSoftInputMode(5)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogClosed(mWhichButtonClicked == DialogInterface.BUTTON_POSITIVE)
    }

    open fun onDialogClosed(positiveResult: Boolean) {}

    override fun onClick(dialog: DialogInterface, which: Int) {
        mWhichButtonClicked = which
        onDialogClosed(which == DialogInterface.BUTTON_POSITIVE)
    }

    companion object {

        protected const val ARG_KEY = "key"

        fun newInstance(key: String?): BasePreferenceDialogFragment {
            val fragment = BasePreferenceDialogFragment()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }
}
package com.liangguo.preference.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liangguo.preference.base.BasePreferenceDialogFragment

open class MyListPreferenceDialogFragmentCompat : BasePreferenceDialogFragment() {
    private var mClickedDialogEntryIndex = 0
    private val listPreference: ListPreference
        get() = preference as ListPreference

    override fun onPrepareDialogBuilder(builder: MaterialAlertDialogBuilder?) {
        super.onPrepareDialogBuilder(builder)
        val preference = listPreference
        check(!(preference.entries == null || preference.entryValues == null)) { "ListPreference 必须要有 entries array 和 entryValues array." }
        mClickedDialogEntryIndex = preference.findIndexOfValue(preference.value)
        builder!!.setSingleChoiceItems(
            preference.entries,
            mClickedDialogEntryIndex
        ) { dialog: DialogInterface, which: Int ->
            mClickedDialogEntryIndex = which
            dismiss()
            onClick(dialog, which)
        }

        /*
         * The typical interaction for list-based dialogs is to have
         * click-on-an-item dismiss the dialog instead of the user having to
         * press 'Ok'.
         */builder.setPositiveButton(null, null)
        builder.setNegativeButton(null, null)
        builder.setNeutralButton(null, null)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        val preference = listPreference
        if (positiveResult && mClickedDialogEntryIndex >= 0 && preference.entryValues != null) {
            val value = preference.entryValues[mClickedDialogEntryIndex].toString()
            if (preference.callChangeListener(value)) {
                preference.value = value
            }
        }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        Log.i(TAG, "onClick: $which")
        mClickedDialogEntryIndex = which
        super.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
    }

    companion object {
        protected const val ARG_KEY = "key"
        private const val TAG = "ATEPreferenceDialog"
        fun newInstance(key: String?): MyListPreferenceDialogFragmentCompat {
            val fragment = MyListPreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }
}
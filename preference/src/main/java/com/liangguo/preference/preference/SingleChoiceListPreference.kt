package com.liangguo.preference.preference

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.preference.ListPreference
import com.liangguo.preference.R


/**
 * @author ldh
 * 时间: 2022/1/28 10:53
 * 邮箱: 2637614077@qq.com
 */
class SingleChoiceListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : ListPreference(context, attrs, defStyleAttr, defStyleRes) {

    var enableDontAskAgain = false

    init {
        layoutResource = R.layout.item_common_preference
        initAttrs(attrs, defStyleAttr, defStyleRes)
    }

    @SuppressLint("RestrictedApi")
    private fun initAttrs(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.DontAskAgainListPreference, defStyleAttr, 0)
        enableDontAskAgain = typedArray.getBoolean(R.styleable.DontAskAgainListPreference_enableDontAskAgain, enableDontAskAgain)
        typedArray.recycle()


    }


}
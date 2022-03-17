package com.liangguo.preference.preference

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.textview.MaterialTextView
import com.liangguo.preference.R


/**
 * @author ldh
 * 时间: 2022/1/24 10:20
 * 邮箱: 2637614077@qq.com
 *
 * 这种Preference可以显示右边的箭头符号，并且可以在右边显示当前的值
 */
open class CommonPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : Preference(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * 放在结尾的TextView
     */
    var endTextView: MaterialTextView? = null

    /**
     * 结尾的文字的数据
     */
    var endText: String = ""
        set(value) = run {
            field = value
            endTextView?.text = value
        }

    lateinit var titleTextView: TextView

    /**
     * 最右边的箭头
     */
    var rightIconView: AppCompatImageView? = null

    /**
     * 是否展示右边的箭头
     */
    var showRightArrowIcon: Boolean = true
        set(value) = run {
            field = value
            rightIconView?.visibility = if (value) View.VISIBLE else View.GONE
        }

    init {
        layoutResource = R.layout.item_common_preference
        initAttrs(attrs, defStyleAttr)
    }

    private fun initAttrs(
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CommonPreference, defStyleAttr, 0)
        typedArray.getString(R.styleable.CommonPreference_endText)?.let {
            endText = it
        }
        showRightArrowIcon = typedArray.getBoolean(
            R.styleable.CommonPreference_showRightArrowIcon,
            showRightArrowIcon
        )
        typedArray.recycle()
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        titleTextView = holder.itemView.findViewById(android.R.id.title)
        endTextView = holder.itemView.findViewById(R.id.textView_end)
        endText = endText
        rightIconView = holder.itemView.findViewById(R.id.imageView_right_icon)
        showRightArrowIcon = showRightArrowIcon
    }

}
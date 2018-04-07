package com.gane.shoper.util

import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.widget.TextView

/**
 *
 */
object ViewUtils {

    fun setHitText(view: TextView, text: String, dpSize: Int) {
        val textSpan = SpannableString(text)
        textSpan.setSpan(AbsoluteSizeSpan(dpSize, true), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.hint = textSpan
    }


}
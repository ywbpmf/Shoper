package com.gane.shoper.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 *
 */
class HeWLinearLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}
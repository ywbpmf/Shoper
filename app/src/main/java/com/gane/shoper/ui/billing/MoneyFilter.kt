package com.gane.shoper.ui.billing

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern
import android.text.TextUtils



/**
 * 输入金额的filter
 */
class MoneyFilter : InputFilter {

    //输入的最大金额
    private val MAX_VALUE = Integer.MAX_VALUE
    //小数点后的位数
    private val POINTER_LENGTH = 2

    private val POINTER = "."

    private val ZERO = "0"


    private val pattern = Pattern.compile("([0-9]|\\.)*");


    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned,
                        dstart: Int, dend: Int): CharSequence {
        val sourceText = source.toString()
        val destText = dest.toString()

        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            return ""
        }

        val matcher = pattern.matcher(source)
        //已经输入小数点的情况下，只能输入数字
        if (destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return ""
            } else {
                if (POINTER == source.toString()) {  //只能输入一个小数点
                    return ""
                }
            }

            //验证小数点精度，保证小数点后只能输入两位
            val index = destText.indexOf(POINTER)
            val length = dend - index

            if (length > POINTER_LENGTH) {
                return dest.subSequence(dstart, dend)
            }
        } else {
            /**
             * 没有输入小数点的情况下，只能输入小数点和数字
             * 1. 首位不能输入小数点
             * 2. 如果首位输入0，则接下来只能输入小数点了
             */
            if (!matcher.matches()) {
                return ""
            } else {
                if (POINTER.equals(source.toString()) && TextUtils.isEmpty(destText)) {  //首位不能输入小数点
                    return ""
                } else if (!POINTER.equals(source.toString()) && ZERO.equals(destText)) { //如果首位输入0，接下来只能输入小数点
                    return ""
                }
            }
        }

        //验证输入金额的大小
        val sumText = java.lang.Double.parseDouble(destText + sourceText)
        return if (sumText > MAX_VALUE) {
            dest.subSequence(dstart, dend)
        } else dest.subSequence(dstart, dend).toString() + sourceText

    }


}
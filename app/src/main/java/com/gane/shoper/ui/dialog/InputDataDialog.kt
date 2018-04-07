package com.gane.shoper.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import com.gane.shoper.R

/**
 * 输入价格和数量的dialog
 */
class InputDataDialog(val ctx: Context, val title: String) : AlertDialog(ctx) {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_input_data)
    }


}
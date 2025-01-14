package com.example.smamysuperalarm.utils

import android.app.Dialog
import android.widget.LinearLayout

object Util {
    fun Dialog.setupDialog(layoutResId: Int) {
        setContentView(layoutResId)
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        setCancelable(false)
    }
}
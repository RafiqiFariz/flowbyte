package com.flowbyte.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.Toolbar

class CustomToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Override onTouchEvent to not consume touch events
        return false
    }
}

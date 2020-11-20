package com.example.customviews

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat

class EditTextWithClear @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr) {
    private var iconDrawable: Drawable? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.EditTextWithClear, 0, 0)
            .apply {
                try {
                    val iconID = getResourceId(R.styleable.EditTextWithClear_clearIcon, 0)
                    if (iconID != 0) {
                        iconDrawable = ContextCompat.getDrawable(context, iconID)
                    }
                } finally {
                    recycle()
                }
            }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        toggleClearIcon()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e ->
            iconDrawable?.let {
                if (e.action == MotionEvent.ACTION_UP
                    && e.x > width - it.intrinsicWidth - 20
                    && e.x < width + 20
                    && e.y > height / 2 - it.intrinsicHeight / 2 - 20
                    && e.y < height / 2 + it.intrinsicHeight / 2 + 20
                ) {
                    text?.clear()
                }
            }
        }
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        toggleClearIcon()
    }

    private fun toggleClearIcon() {
        val icon = if (isFocused && text?.isNotEmpty() == true) iconDrawable else null
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null)
    }
}
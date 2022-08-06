package com.dicoding.theozu.mystoryapp.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.theozu.mystoryapp.R

class PasswordEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var passwordIconImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        passwordIconImage = ContextCompat.getDrawable(context, R.drawable.ic_lock_black_24) as Drawable

        setDrawables(startOfTheText = passwordIconImage)

        transformationMethod = PasswordTransformationMethod.getInstance()

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.isNotEmpty()) {
                    if (p0.length < 6) {
                        error = "Password must consist of at least 6 characters"
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun setDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(p0: View, p1: MotionEvent): Boolean {
        return false
    }
}
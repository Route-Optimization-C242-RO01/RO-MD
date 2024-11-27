package com.example.myrouteoptimization.ui.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.myrouteoptimization.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditTextCv @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : TextInputEditText(context, attributeSet) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val parentLayout = parent.parent as TextInputLayout

                if(p0.toString().length < 8) {
                    parentLayout.helperText = context.getString(R.string.required_password_length_message)
                    parentLayout.setHelperTextColor(
                        ContextCompat.getColorStateList(context, R.color.md_theme_error)
                    )
                } else {
                    parentLayout.helperText = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }
}
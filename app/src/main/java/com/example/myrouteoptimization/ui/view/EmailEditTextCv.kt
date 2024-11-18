package com.example.myrouteoptimization.ui.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns.EMAIL_ADDRESS
import androidx.core.content.ContextCompat
import com.example.myrouteoptimization.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailEditTextCv @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : TextInputEditText(context, attributeSet) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let { input ->
                    val getHelper = parent.parent as TextInputLayout
                    if (EMAIL_ADDRESS.matcher(input).matches()) {
                        getHelper.helperText = null
                    } else {
                        setError(context.getString(R.string.false_email_pattern), null)
                        getHelper.helperText = context.getString(R.string.false_email_pattern)
                        getHelper.setHelperTextColor(
                            ContextCompat.getColorStateList(context, R.color.md_theme_error)
                        )
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }
}
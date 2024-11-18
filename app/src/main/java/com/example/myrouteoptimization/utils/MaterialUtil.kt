package com.example.myrouteoptimization.utils

import android.content.Context
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showMaterialDialog(
    context: Context,
    title : String,
    message :String,
    positiveButtonText : String,
    onPositiveClick : (() -> Unit)? = null,
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText) { dialog, _ ->
            onPositiveClick?.invoke()
            dialog.dismiss()
        }
        .setCancelable(true)
        .show()
}

fun showToast(context: Context, message: String, duration : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}
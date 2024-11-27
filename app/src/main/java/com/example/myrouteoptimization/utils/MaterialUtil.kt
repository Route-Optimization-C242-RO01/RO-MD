package com.example.myrouteoptimization.utils

import android.content.Context
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Displays a Material Design dialog with a title, message, and a positive button.
 *
 * This function shows a dialog with a customizable title, message, and positive button text.
 * When the positive button is clicked, the optional `onPositiveClick` callback will be invoked if provided.
 *
 * @param context The Context to display the dialog. Typically used with an Activity or Application context.
 * @param title The title of the dialog to be displayed.
 * @param message The message or description to be displayed in the dialog.
 * @param positiveButtonText The text displayed on the positive button (e.g., "OK").
 * @param onPositiveClick An optional callback that will be called when the positive button is clicked.
 * If not provided, the dialog will simply dismiss when the positive button is clicked without further action.
 */
fun showMaterialDialog(
    context: Context,
    title: String,
    message:String,
    positiveButtonText: String,
    onPositiveClick: (() -> Unit?)? = null,
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


/**
 * Displays a short or long toast message based on the specified duration.
 *
 * This function is used to display a brief message on the screen that disappears
 * after a few seconds. You can customize the duration of the toast as needed.
 *
 * @param context The Context to show the Toast. Typically used with an Activity or Application context.
 * @param message The message to be displayed in the Toast.
 * @param duration The duration for which the Toast will be displayed. Can be either `Toast.LENGTH_SHORT` or `Toast.LENGTH_LONG`. Default is `Toast.LENGTH_SHORT`.
 */
fun showToast(context: Context, message: String, duration : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}
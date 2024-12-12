package dev.liinahamari.suggestions_ui

import android.content.Context
import android.graphics.Color
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

fun Context.startCircularProgress() = CircularProgressDrawable(this).apply {
    setStyle(CircularProgressDrawable.DEFAULT)
    setColorSchemeColors(Color.parseColor("#ffbb87fc"))
    start()
}

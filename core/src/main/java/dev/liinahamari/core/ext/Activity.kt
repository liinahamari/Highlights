package dev.liinahamari.core.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.getActivity(): Activity? = if (this is ContextWrapper) {
    if (this is Activity) {
        this
    } else {
        baseContext?.getActivity()
    }
} else null

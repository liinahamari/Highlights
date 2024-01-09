package dev.liinahamari.list_ui.single_entity.add_dialogs

import android.content.Context
import android.util.AttributeSet
import android.webkit.URLUtil
import com.google.android.material.R
import com.google.android.material.textfield.TextInputEditText

class UrlValidatingEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : TextInputEditText(context, attributeSet, defStyleAttr) {
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setOnFocusChangeListener { _, hasFocus: Boolean ->
            if (text.isNullOrEmpty() || hasFocus) return@setOnFocusChangeListener
            error = if (URLUtil.isNetworkUrl(text.toString())) {
                null
            } else {
                context.getString(dev.liinahamari.list_ui.R.string.url_is_not_valid)
            }
        }
    }
}

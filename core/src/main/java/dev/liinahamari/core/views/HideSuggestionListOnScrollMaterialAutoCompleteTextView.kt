package dev.liinahamari.core.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.AutoCompleteTextView
import android.widget.ListPopupWindow
import androidx.annotation.CallSuper
import androidx.appcompat.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dev.liinahamari.core.ext.hideKeyboard
import dev.liinahamari.core.ext.keyboardIsVisible

open class HideSuggestionListOnScrollMaterialAutoCompleteTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : MaterialAutoCompleteTextView(context, attributeSet, defStyleAttr) {
    @SuppressLint("DiscouragedPrivateApi")
    @Suppress("LeakingThis")
    private var popupWindow: ListPopupWindow? = (AutoCompleteTextView::class.java.getDeclaredField("mPopup")
        .also { it.isAccessible = true }).get(this) as? ListPopupWindow

    @SuppressLint("ClickableViewAccessibility")
    @CallSuper
    override fun showDropDown() {
        super.showDropDown()
        popupWindow?.listView?.setOnTouchListener { _, event: MotionEvent ->
            if (keyboardIsVisible && event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        popupWindow = null
        super.onDetachedFromWindow()
    }
}

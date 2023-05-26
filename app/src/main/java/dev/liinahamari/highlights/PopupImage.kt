package dev.liinahamari.highlights

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

class ImagePopup : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun show(imageUrl: String?, view: View) {
        val layout =
            (view.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.popup, null)
                .apply { setBackgroundColor(Color.BLACK) }
        val imageView = layout.findViewById<View>(R.id.imageView) as ImageView
        Glide.with(view.context).load(imageUrl).into(imageView)
        PopupWindow(layout, MATCH_PARENT, MATCH_PARENT, true).apply {
            showAtLocation(view, Gravity.CENTER, 0, 0)
            layout.findViewById<ImageView>(R.id.closeBtn).setOnClickListener { dismiss() }
            dimBehind(this)
            imageView.setOnClickListener { dismiss() }
        }
    }

    companion object {
        fun dimBehind(popupWindow: PopupWindow) {
            val container: View = if (popupWindow.background == null) {
                popupWindow.contentView.parent as View
            } else {
                popupWindow.contentView.parent.parent as View
            }
            with(popupWindow.contentView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager) {
                updateViewLayout(container, (container.layoutParams as LayoutParams).apply {
                    flags = LayoutParams.FLAG_DIM_BEHIND
                    dimAmount = 0.3f
                })
            }
        }
    }
}

package dev.liinahamari.highlights

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.graphics.Color
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

class PopupImage constructor(context: Context) : AppCompatImageView(context) {
    @SuppressLint("InflateParams") fun show(imageUrl: String?, viewToAttachTo: View) {
        val layout =
            (viewToAttachTo.context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.popup,
                null
            ).apply { setBackgroundColor(Color.BLACK) }
        val imageView = layout.findViewById<View>(R.id.imageView) as ImageView

        Glide.with(viewToAttachTo.context).load(imageUrl).into(imageView)

        PopupWindow(layout, MATCH_PARENT, MATCH_PARENT, true).apply {
            showAtLocation(viewToAttachTo, Gravity.CENTER, 0, 0)
            layout.findViewById<ImageView>(R.id.closeBtn).setOnClickListener { dismiss() }
            dimBehind()
            imageView.setOnClickListener { dismiss() }
        }
    }

    private fun PopupWindow.dimBehind() {
        val container: View = if (background == null) {
            contentView.parent as View
        } else {
            contentView.parent.parent as View
        }
        with(contentView.context.getSystemService(WINDOW_SERVICE) as WindowManager) {
            updateViewLayout(container, (container.layoutParams as LayoutParams).apply {
                flags = LayoutParams.FLAG_DIM_BEHIND
                dimAmount = 0.3f
            })
        }
    }
}

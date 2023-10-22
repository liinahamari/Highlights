package dev.liinahamari.list_ui.custom_views

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import dev.liinahamari.list_ui.databinding.PopupBinding

class PopupImage constructor(context: Context) : AppCompatImageView(context) {
    fun show(imageUrl: String?, viewToAttachTo: View) {
        with(PopupBinding.inflate(LayoutInflater.from(context))) {
            Glide.with(viewToAttachTo.context).load(imageUrl).into(imageView)

            PopupWindow(root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true).apply {
                showAtLocation(viewToAttachTo, Gravity.CENTER, 0, 0)
                closeBtn.setOnClickListener { dismiss() }
                dimBehind()
                imageView.setOnClickListener { dismiss() }
            }
        }
    }

    private fun PopupWindow.dimBehind() {
        val container: View = if (background == null) {
            contentView.parent as View
        } else {
            contentView.parent.parent as View
        }
        with(contentView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager) {
            updateViewLayout(container, (container.layoutParams as WindowManager.LayoutParams).apply {
                flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                dimAmount = 0.3f
            })
        }
    }
}

package dev.liinahamari.highlights

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import dev.liinahamari.highlights.databinding.PopupBinding

class PopupImage constructor(context: Context) : AppCompatImageView(context) {
    fun show(imageUrl: String?, viewToAttachTo: View) {
        with(PopupBinding.inflate(LayoutInflater.from(context))) {
            Glide.with(viewToAttachTo.context).load(imageUrl).into(imageView)

            PopupWindow(root, MATCH_PARENT, MATCH_PARENT, true).apply {
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
        with(contentView.context.getSystemService(WINDOW_SERVICE) as WindowManager) {
            updateViewLayout(container, (container.layoutParams as LayoutParams).apply {
                flags = LayoutParams.FLAG_DIM_BEHIND
                dimAmount = 0.3f
            })
        }
    }
}

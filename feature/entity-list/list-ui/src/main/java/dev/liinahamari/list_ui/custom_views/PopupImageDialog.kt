package dev.liinahamari.list_ui.custom_views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import dev.liinahamari.core.views.FullScreenDialogFragment
import dev.liinahamari.list_ui.R
import dev.liinahamari.list_ui.databinding.PopupBinding

class PopupImageDialog(private val posterUrl: String) : FullScreenDialogFragment() {
    private var _ui: PopupBinding? = null
    private val ui get() = _ui!!

    override fun onCreateDialog(savedInstanceState: Bundle?) = Dialog(requireActivity()).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.popup, container, false).also {
        _ui = PopupBinding.bind(it)
    }

    override fun getBinding(): ViewBinding = ui

    override fun onDestroyView() {
        super.onDestroyView()
        _ui = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        ui.closeBtn.setOnClickListener { _ -> dismiss() }
        ui.posterIv.setOnClickListener { _ -> dismiss() }
        Glide.with(ui.root.context).load(posterUrl).into(ui.posterIv)
    }
}

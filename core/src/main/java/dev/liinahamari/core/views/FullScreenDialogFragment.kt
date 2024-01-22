@file:Suppress("DEPRECATION")

package dev.liinahamari.core.views

import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.CallSuper
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import dev.liinahamari.core.ext.getActivity

/** https://developer.android.com/training/system-ui/status */
abstract class FullScreenDialogFragment : DialogFragment() {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    abstract fun getBinding(): ViewBinding

    private fun hideSystemUI() = context?.getActivity()?.apply {
        window?.decorView?.systemUiVisibility = SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        getBinding().root.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_FULLSCREEN
                or SYSTEM_UI_FLAG_IMMERSIVE)
        setDecorFitsSystemWindows(requireActivity().window, false)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        showSystemUi()
    }

    private fun showSystemUi() {
        setDecorFitsSystemWindows(requireActivity().window, true)
        context?.getActivity()?.apply {
            window?.decorView?.systemUiVisibility = SYSTEM_UI_FLAG_VISIBLE
            actionBar?.show()
        }
    }
}

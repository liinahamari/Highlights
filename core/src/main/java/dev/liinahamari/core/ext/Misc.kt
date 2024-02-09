package dev.liinahamari.core.ext

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import java.io.File

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View = from(context).inflate(layoutRes, this, false)

//val Context.appComponent: dev.liinahamari.impl.di.components.MainComponent
//    get() = when (this) {
//        is App -> appComponent
//        else -> applicationContext.appComponent
//    }

//val Fragment.appComponent: dev.liinahamari.impl.di.components.MainComponent?
//    get() = context?.appComponent

fun ViewPager2.getCurrentFragment(fm: FragmentManager): Fragment = fm.findFragmentByTag("f${currentItem}")!!

inline fun <reified T> Bundle.getParcelableOf(key: String): T =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)!!
    } else {
        @Suppress("DEPRECATION") getParcelable(key)!!
    }

fun Context.toast(text: String) = Toast.makeText(this, text, LENGTH_SHORT).show()
fun Fragment.toast(text: String) = requireContext().toast(text)

fun Context.getActivity(): Activity? = if (this is ContextWrapper) {
    if (this is Activity) {
        this
    } else {
        baseContext?.getActivity()
    }
} else null

fun Fragment.getDatabasePath(dbName: String): File = requireContext().getDatabasePath(dbName)

/** Implied that app had implemented single activity pattern */
fun Fragment.restartApp(mainActivityClass: Class<out AppCompatActivity>) {
    requireActivity().apply {
        startActivity(Intent(this, mainActivityClass).addFlags(FLAG_ACTIVITY_NEW_TASK))
        finish()
        Runtime.getRuntime().exit(0)
    }
}

val Fragment.contentResolver: ContentResolver
    get() = requireContext().contentResolver

val Context.layoutInflater: LayoutInflater
    get() = getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

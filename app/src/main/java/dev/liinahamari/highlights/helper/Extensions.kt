package dev.liinahamari.highlights.helper

import android.content.Context
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import dev.liinahamari.highlights.App
import dev.liinahamari.highlights.di.components.MainComponent

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View = from(context).inflate(layoutRes, this, false)

val Context.appComponent: MainComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }

val Fragment.appComponent: MainComponent?
    get() = context?.appComponent

fun ViewPager2.getCurrentFragment(fm: FragmentManager): Fragment = fm.findFragmentByTag("f${currentItem}")!!

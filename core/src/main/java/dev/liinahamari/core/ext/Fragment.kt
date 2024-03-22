package dev.liinahamari.core.ext

import android.content.ContentResolver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import java.io.File

fun ViewPager2.getCurrentFragment(fm: FragmentManager): Fragment = fm.findFragmentByTag("f${currentItem}")!!

fun Fragment.getDatabasePath(dbName: String): File = requireContext().getDatabasePath(dbName)

/** Implied that app had implemented single activity pattern */
fun Fragment.restartApp(mainActivityClass: Class<out AppCompatActivity>) {
    requireActivity().apply {
        startActivity(Intent(this, mainActivityClass).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
        Runtime.getRuntime().exit(0)
    }
}

val Fragment.contentResolver: ContentResolver
    get() = requireContext().contentResolver

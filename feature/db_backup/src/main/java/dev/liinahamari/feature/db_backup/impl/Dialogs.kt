package dev.liinahamari.feature.db_backup.impl

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun FragmentActivity.providePermissionExplanationDialog(
    title: String,
    message: String
): AlertDialog =
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(getString(android.R.string.ok), null)
        .setNegativeButton("Settings") { dialog, _ ->
            dialog.dismiss()
            openAppSettings()
        }
        .create()

fun Fragment.providePermissionExplanationDialog(
    title: String,
    message: String
): AlertDialog =
    MaterialAlertDialogBuilder(requireActivity())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(getString(android.R.string.ok), null)
        .setNegativeButton("Settings") { dialog, _ ->
            dialog.dismiss()
            requireActivity().openAppSettings()
        }
        .create()

private fun FragmentActivity.openAppSettings() = startActivity(
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    )
)

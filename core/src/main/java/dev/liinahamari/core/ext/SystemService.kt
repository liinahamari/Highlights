package dev.liinahamari.core.ext

import android.app.Activity
import android.content.Context
import android.hardware.SensorManager
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

val Context.layoutInflater: LayoutInflater
    get() = getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
val Context.sensorManager: SensorManager
    get() = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

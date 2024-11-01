package dev.liinahamari.api.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize data class Country(val iso: String, val name: String) : Parcelable

fun String.fromIsoCode() = Country(iso = this, name = Locale.ENGLISH.getDisplayCountry(Locale("en", this)))

package dev.liinahamari.api.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize data class Country(val iso: String, val name: String) : Parcelable

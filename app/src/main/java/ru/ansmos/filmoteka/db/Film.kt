package ru.ansmos.filmoteka.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film (val id: Int,val title: String, val poster: Int, val description: String) : Parcelable

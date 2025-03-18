package ru.ansmos.filmoteka.bll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film (
    val id: String,
    val title: String,
    val poster: String,
    val description: String,
    val releaseDate: String,
    var rating: Double = 0.0,
    var isInFavorites: Boolean = false,
    var nn: Int = 0
) : Parcelable

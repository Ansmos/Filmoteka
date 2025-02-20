package ru.ansmos.filmoteka.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

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

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
    val year: String,
    var rating: Float = 0f,
    var isInFavorites: Boolean = false
) : Parcelable

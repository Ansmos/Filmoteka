package ru.ansmos.filmoteka.db

import com.google.gson.annotations.SerializedName

data class TmdbFilmListDTO (
    @SerializedName("page") val page: Int,
    @SerializedName("results") val tmdbFilmList: List<TmdbFilmDTO>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

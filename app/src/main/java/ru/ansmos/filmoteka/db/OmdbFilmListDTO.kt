package ru.ansmos.filmoteka.db

import com.google.gson.annotations.SerializedName

data class OmdbFilmListDTO(
    @SerializedName("Response")
    val success: String,
    @SerializedName("Search")
    val omdbFilmList: List<OmdbFilmDTO>,
    @SerializedName("totalResults")
    val totalResults: String,
    @Transient
    val page : Int
)
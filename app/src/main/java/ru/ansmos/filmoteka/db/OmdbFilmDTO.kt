package ru.ansmos.filmoteka.db

import com.google.gson.annotations.SerializedName

data class OmdbFilmDTO(
    @SerializedName("imdbID")
    val id: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("Poster")
    val poster: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Year")
    val year: String,
    @Transient
    val rating : Float

)
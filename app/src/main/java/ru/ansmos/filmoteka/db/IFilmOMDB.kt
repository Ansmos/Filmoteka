package ru.ansmos.filmoteka.db

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IFilmOMDB {
    @GET("/")
    fun getFilm(
        @Query("i") id: String,
        @Query("apikey") key: String
    ) : Call<FilmOMDB>
}
package ru.ansmos.filmoteka.db

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IOmdbApi {
    @GET("/")
    fun getFilm(
        @Query("i") id: String,
        @Query("apikey") key: String
    ): Call<OmdbFilmDTO>
    @GET("/")
    fun getFilmList(
        @Query("s") id: String,
        @Query("apikey") key: String
    ) : Call<OmdbFilmListDTO>
}
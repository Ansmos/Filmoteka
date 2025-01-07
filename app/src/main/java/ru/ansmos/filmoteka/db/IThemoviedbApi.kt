package ru.ansmos.filmoteka.db

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IThemoviedbApi {
    @GET("3/movie/{category}")
    fun getFilmList(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TmdbFilmListDTO>
}
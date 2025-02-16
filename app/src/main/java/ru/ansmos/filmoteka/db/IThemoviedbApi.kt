package ru.ansmos.filmoteka.db

import io.reactivex.rxjava3.core.Observable
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

    @GET("3/movie/{category}")
    fun getFilmListRx(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Observable<TmdbFilmListDTO>

}
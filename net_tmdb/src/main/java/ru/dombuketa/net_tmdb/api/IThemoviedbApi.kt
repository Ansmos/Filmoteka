package ru.dombuketa.net_tmdb.api

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.dombuketa.net_tmdb.imp.TmdbFilmDTO
import ru.dombuketa.net_tmdb.imp.TmdbFilmListDTO

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

    @GET("3/search/movie")
    fun getFilmsFromSearch(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Observable<TmdbFilmListDTO>

    fun getPopularFilmsRx(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ) : Single<TmdbFilmListDTO>

    @GET("3/movie/{id}")
    fun getFilm(
        @Path("id") category: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Observable<TmdbFilmDTO>
}
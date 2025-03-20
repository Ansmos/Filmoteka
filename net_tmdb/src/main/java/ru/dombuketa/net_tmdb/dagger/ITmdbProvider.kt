package ru.dombuketa.net_tmdb.dagger

import ru.dombuketa.net_tmdb.api.IThemoviedbApi

interface ITmdbProvider {
    fun provideTmdb(): IThemoviedbApi
}